package com.stream.nz.util;

import com.stream.nz.exception.EncryptException;
import com.stream.nz.model.RsaVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaUtil {
    private static final Logger log = LoggerFactory.getLogger(RsaUtil.class);

    private static int keySize = 1024;

    public RsaUtil(int keySize) {
        this.keySize = keySize;
    }

    public static BigInteger[] genKeys() throws Exception {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(keySize / 2 - 1, random);
        BigInteger q = BigInteger.probablePrime(keySize / 2 - 1, random);
        BigInteger e = BigInteger.probablePrime(keySize / 2, random);
        return genKeys(p, q, e);
    }

    public static RsaVO genRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair =  keyPairGen.generateKeyPair();

        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RsaVO newRsaVo = new RsaVO();
        //私钥指数hex
        newRsaVo.setRsaPrivateExponent(privateKey.getPrivateExponent().toString(16));
        //模hex
        newRsaVo.setRsaModules(publicKey.getModulus().toString(16));
        //公钥指数hex
        newRsaVo.setRsaPublicExponent(publicKey.getPublicExponent().toString(16));

        return newRsaVo;
    }


    public static BigInteger[] genKeys(BigInteger p, BigInteger q, BigInteger e) throws Exception {
        if (e.compareTo(BigInteger.ONE) <= 0) {
            throw new Exception("e must be larger than 1");
        }

        BigInteger[] keys = new BigInteger[3];

        // 计算模数p*q
        BigInteger n = p.multiply(q);

        // 计算(p-1)*(q-1) = n-p-q+1
        BigInteger f = n.subtract(p).subtract(q).add(BigInteger.ONE); // (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        if (e.compareTo(f) >= 0) {
            throw new Exception("e must be smaller than (p-1)*(q-1)");
        }

        if (f.gcd(e).compareTo(BigInteger.ONE) != 0) {
            throw new Exception("e must be coprime with (p-1)*(q-1)");
        }

        // 计算出d，即e的模n逆
        BigInteger d = e.modInverse(f);

        // 公钥
        keys[0] = e;
        System.out.println("e: "+ e);

        // 私钥
        keys[1] = d;

        // 模数
        keys[2] = n;
        System.out.println("n: "+ n);

        return keys;
    }

    public static byte[] encode(byte[] source, BigInteger key, BigInteger modulus) throws EncryptException {
        int blockSize = keySize / 8;
        int inBlockSize = blockSize - 11;
        int offSet = 0;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            while (source.length > offSet) {
                int inputLen = Math.min(source.length - offSet, inBlockSize);
                byte[] cache = encodeBlock(source, offSet, inputLen, key, modulus, blockSize);
                dos.writeInt(cache.length);
                dos.write(cache);
                offSet += inputLen;
            }
            dos.close();
        }catch (EncryptException e){
            log.error(e.getMessage());
        } catch (IOException e){
            log.error(e.getMessage());
        }
        return baos.toByteArray();
    }

    public static byte[] encodeByPrivate(byte[] source, BigInteger key, BigInteger modulus) throws EncryptException  {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            BigInteger message = new BigInteger(source);
            if(message.compareTo(modulus) > 0){
                throw new EncryptException("the message must be smaller than the module");
            }
            // 加密，计算 (message ^ key) % modulus
            BigInteger encrypt = message.modPow(key, modulus);
            byte[] encodeBytes = encrypt.toByteArray();
            dos.write(encodeBytes);
            dos.close();
        }catch (EncryptException e){
            log.error(e.getMessage());
        } catch (IOException e){
            log.error(e.getMessage());
        }
        return baos.toByteArray();
    }


    public static byte[] encodeByPubKeyNoPadding(byte[] source, BigInteger key, BigInteger modulus){
        BigInteger message = new BigInteger(source);
        if(message.compareTo(modulus) > 0){
            throw new EncryptException("the message must be smaller than the module");
        }
        // 加密，计算 (message ^ key) % modulus
        BigInteger encrypt = message.modPow(key, modulus);
        return encrypt.toByteArray();
    }


    private static byte[] encodeBlock(byte[] bytes, int offset, int len, BigInteger key, BigInteger modulus,
                               int blockSize) throws EncryptException {
        byte[] source = bytes;
        if(bytes.length != len || offset != 0) {
            source = new byte[len];
            System.arraycopy(bytes, offset, source, 0, len);
        }

        byte[] padding = paddingBlock(source, blockSize);
        BigInteger message = new BigInteger(padding);
        if(message.compareTo(modulus) > 0){
            throw new EncryptException("the message must be smaller than the module");
        }

        // 加密，计算 (message ^ key) % modulus
        BigInteger encrypt = message.modPow(key, modulus);
        return encrypt.toByteArray();
    }

    private static byte[] paddingBlock(byte[] source, int blockSize) throws EncryptException {
        if(source.length > (blockSize - 1)) {
            throw new EncryptException("Message too large");
        }
        byte[] padding = new byte[blockSize];
        padding[0] = 1;
        int len = source.length;
        padding[1] = (byte) (len >> 24);
        padding[2] = (byte) (len >> 16);
        padding[3] = (byte) (len >> 8);
        padding[4] = (byte) len;
        System.arraycopy(source, 0, padding, blockSize - len, len);
        return padding;
    }

    public static byte[] decode(byte[] source, BigInteger key, BigInteger modulus) throws EncryptException {
        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        DataInputStream dis = new DataInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            byte[] cache = decodeBlock(source, key, modulus);
            baos.write(cache);
            dis.close();
            baos.close();
        }catch (Exception e){
            throw new EncryptException(e.getMessage());
        }
        return baos.toByteArray();
    }

    public static byte[] decodeBlock(byte[] source, BigInteger key, BigInteger modulus) {
        BigInteger cipherMessage = new BigInteger(source);
        // 解密， 计算(cipher ^ key) % modulus
        BigInteger sourceMessage = cipherMessage.modPow(key, modulus);
        byte[] decodeBytes =  sourceMessage.toByteArray();
        return decodeBytes;
    }

    private static byte[] recoveryPaddingBlock(byte[] padding) throws Exception {
        if(padding[0] != 1){
            throw new Exception("Not RSA Block");
        }
        int len = ((padding[1] & 0xff) << 24) + ((padding[2] & 0xff) << 16) + ((padding[3] & 0xff) << 8) + (padding[4] & 0xff);
        byte[] data = new byte[len];
        System.arraycopy(padding, padding.length - len, data, 0, len);
        return data;
    }

    public static byte[] encodeWithNoPadding(byte[] source,BigInteger key,BigInteger modulus){
        BigInteger message = new BigInteger(source);
        if(message.compareTo(modulus) > 0){
            throw new EncryptException("the message must be smaller than the module");
        }

        // 加密，计算 (message ^ key) % modulus
        BigInteger encrypt = message.modPow(key, modulus);
        return encrypt.toByteArray();
    }


    public static void main(String[] args) {
        RsaUtil smrsa = new RsaUtil(1024);
        try {
            BigInteger[] bigIntegers =  smrsa.genKeys();
            BigInteger _public = bigIntegers[0];
            BigInteger _private = bigIntegers[1];
            BigInteger _modules = bigIntegers[2];

            log.info("public=="+_public.toString(10));
            log.info("private=="+_private.toString(10));
            log.info("modules=="+_modules.toString(10));

            byte[] bytes = new String("19779ABVCDECSa4a").getBytes();
            byte[] encode1 = smrsa.encodeByPrivate(bytes,_private,_modules);
            byte[] decode = smrsa.decode(encode1, _public, _modules);
            log.info(new String(decode));

            byte[] encrypt = smrsa.encodeByPubKeyNoPadding(bytes,_public,_modules);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("Throwable",throwable);
        }
    }

}
