package com.stream.nz.encrypt.util;

import com.stream.nz.encrypt.exception.EncryptException;
import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.token.JwtTools;
import com.stream.nz.token.model.dto.OverseaClaims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Slf4j
public class RsaAesUtils {

    private static final Logger logger = LoggerFactory.getLogger( RsaAesUtils.class);

    /**
     * RSA私钥解密
     *
     * @param source
     * @return 解密后的 byte[]
     * @throws Throwable
     */
    public static byte[] rsaDecodeByPrivateKey(byte[] source, RSAResource rsaResource)  {
//        logger.info("e: "+rsaResource.getBigPublicKey());
//        logger.info("n: "+rsaResource.getBigModule());
        return RsaUtil.decodeBlock(source, rsaResource.getBigPrivateKey(), rsaResource.getBigModule());
    }

    /**
     * RSA私钥加密
     *
     * @param source
     * @return 加密后的 byte[]
     * @throws Throwable
     */
    public static byte[] rsaEncodeByPrivateKey(byte[] source, RSAResource rsaResource)  {
        return RsaUtil.encode(source, rsaResource.getBigPrivateKey(), rsaResource.getBigModule());
    }

    public static byte[] rsaEncodeByPrivateKeyNoPadding(byte[] source, RSAResource rsaResource) {
        return RsaUtil.encodeByPrivate(source,rsaResource.getBigPrivateKey(),rsaResource.getBigModule());
    }

    /**
     * RSA公钥解密
     *
     * @param source
     * @return 解密后的 byte[]
     * @throws Throwable
     */
    public static byte[] rsaDecodeByPublicKey(byte[] source, RSAResource rsaResource) {
        return RsaUtil.decode(source, rsaResource.getBigPublicKey(), rsaResource.getBigModule());
    }

    /**
     * RSA公钥加密
     *
     * @param source
     * @return 解密后的 byte[]
     * @throws Throwable
     */
    public static byte[] rsaEncodeByPublicKey(byte[] source, RSAResource rsaResource) {
        return RsaUtil.encodeWithNoPadding(source, rsaResource.getBigPublicKey(), rsaResource.getBigModule());
    }

    /**
     * 加密协议=公钥+AES加密
     * @return 
     * @throws Throwable
     */
    public static String encryptByPublicKeyAes(String json, RSAResource rsaResource,Integer zip) throws Exception {
        // 生成Aes秘钥 一次有效
        String aesKey = RandomUtils.randomText(16, 16).toLowerCase();
        byte[] aesKeyBytes = aesKey.getBytes(Charset.forName("UTF-8"));

        // AES 加密消息体
        String aesEncryptBodyStrBase64  =  AESUtils.aesEncrypt(json,aesKey);

        int bodyLen = aesEncryptBodyStrBase64.length();
        String bodyLenWithPadZero = lengthPadWithZero(bodyLen);

        // RSA 公钥加密 AesKey
        byte[] encodeAesKeyBytes = RsaAesUtils.rsaEncodeByPublicKey(aesKeyBytes, rsaResource);
        String encodeAesKeyHexStr = bytesToHexString(encodeAesKeyBytes);

        int encodeAesKeyLen = encodeAesKeyHexStr.length();
        String encodeAesKeyLenWithPadZero = lengthPadWithZero(encodeAesKeyLen);


        // 拼接加密消息体
        StringBuilder sb = new StringBuilder();
        sb.append(encodeAesKeyLenWithPadZero).append(encodeAesKeyHexStr).append(bodyLenWithPadZero).append(aesEncryptBodyStrBase64);

        return new String(sb);
    }

    /**
     * 加密协议= 私钥+AES解密
     * @param toDecrypt
     * @param rsaResource
     * @param zip
     * @return
     */
    public static String decryptByPrivateKeyAes(String toDecrypt , RSAResource rsaResource , Integer zip) throws Exception {

        // 协议体非空校验
        if (StringUtils.isEmpty(toDecrypt)) {
            throw new EncryptException("to decrypt is null");
        }

        // 协议体获取AesKey 字节数组长度值
        String aesKeyLenStr = toDecrypt.substring(0,6);
        int aesKeyLen = Integer.parseInt(aesKeyLenStr);

        if (aesKeyLen < 0 || aesKeyLen >= toDecrypt.length()) {
            throw new EncryptException("aes key length wrong");
        }

        // 协议体获取aesKey内容字节数组
        String hexEncryptAesKey = toDecrypt.substring(6,6+aesKeyLen);

        String aesKey = null;
        // Rsa非对称协议解密AesKey
        BigInteger source = new BigInteger(hexEncryptAesKey,16);
        byte[] toByteArray = source.toByteArray();
        byte[] aesKeyBytes = RsaAesUtils.rsaDecodeByPrivateKey(toByteArray,rsaResource);
        if (aesKeyBytes != null && aesKeyBytes.length > 0){
            aesKey = new String(aesKeyBytes, Charset.forName("UTF-8"));
        }

        // 协议体获取加密内容字节数组长度值
        String aesEncryptBodyLen = toDecrypt.substring(6+aesKeyLen,6+aesKeyLen+6);

        int dataLen = Integer.parseInt(aesEncryptBodyLen);

        if (dataLen < 0 || dataLen >= toDecrypt.length()) {
            throw new EncryptException("to decrypt data length wrong");
        }

        // 获取协议体内容字节数组
        String toDecryptBody = toDecrypt.substring(aesKeyLen+12,aesKeyLen+12+dataLen);

        // AES解密内容
        String aesDecryptBody = AESUtils.aesDecrypt(toDecryptBody,aesKey);
        byte[] decryptedBytes = aesDecryptBody.getBytes("UTF-8");

        // 解密消息体字节数组解压缩
        if (zip != null && zip == 1) {
            decryptedBytes = ZipUtils.unzip(decryptedBytes);
        }

        if (decryptedBytes == null || decryptedBytes.length < 1) {
            throw new EncryptException("decrypt by private key exception");
        }


        if (decryptedBytes == null || decryptedBytes.length < 1) {
            throw new EncryptException("body length is null after unzip");
        }
        String decryptStrUtf8 = new String(decryptedBytes, Charset.forName("UTF-8")).trim();
        return decryptStrUtf8;
    }


    /**
     *  加密协议= 私钥+AES 加密
     * @param toEncrypt
     * @param rsaResource
     * @param zip
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateAes(String toEncrypt , RSAResource rsaResource , Integer zip) throws Exception {
        // 生成Aes秘钥 一次有效
        String aesKey = RandomUtils.randomText(16, 16).toLowerCase();
        byte[] aesKeyBytes = aesKey.getBytes(Charset.forName("UTF-8"));

        // 要加密json 得到byte[]
        String aesEncryptBodyStr =  AESUtils.aesEncrypt(toEncrypt,aesKey);
        byte[] body = aesEncryptBodyStr.getBytes("UTF-8");

        // 加密字符串 解压缩
        if (zip != null && zip == 1) {
            body = ZipUtils.zip(body);
        }

        int bodyLen = body.length;
        String bodyLenStrPadding = lengthPadWithZero(bodyLen);

        // RSA 公钥加密 AesKey
        byte[] encodeAesKeyBytes = RsaAesUtils.rsaEncodeByPrivateKeyNoPadding(aesKeyBytes, rsaResource);
        int encodeAesKeyLen = encodeAesKeyBytes.length;
        String encodeAesKeyLenStrPading = lengthPadWithZero(encodeAesKeyLen);

        // 拼接加密消息体
        byte[] data = new byte[12 + encodeAesKeyLen + bodyLen];
        ByteBuffer b = ByteBuffer.allocate(6);

        // AESkey长度值拼接到协议体
        b.put(encodeAesKeyLenStrPading.getBytes("UTF-8"));
        System.arraycopy(b.array(), 0, data, 0, 6);
        // AESkey本身拼接到协议体
        System.arraycopy(encodeAesKeyBytes, 0, data, 6, encodeAesKeyLen);

        // 加密内容长度值拼接到协议体
        b = ByteBuffer.allocate(6);
        b.put(bodyLenStrPadding.getBytes("UTF-8"));
        System.arraycopy(b.array(), 0, data, 6 + encodeAesKeyLen, 6);
        // 加密内容字节数组拼接到协议体
        System.arraycopy(body, 0, data, 12 + encodeAesKeyLen, body.length);

       // 响应 16进制字符串
        String byteToHexStr = bytesToHexString(data);
        return byteToHexStr;
    }

    /**
     * rsa资源转换
     * @param rsaVO
     * @return
     */
    public static  RSAResource transferRsaVoToRsaResource(RsaVO rsaVO){
        RSAResource rsaResource = new RSAResource();
        if (null == rsaVO){
            return null;
        }
        rsaResource.setBigModule(new BigInteger(rsaVO.getRsaModules(), 16));
        rsaResource.setBigPrivateKey(new BigInteger(rsaVO.getRsaPrivateExponent(),16));
        rsaResource.setBigPublicKey(new BigInteger(rsaVO.getRsaPublicExponent(),16));
        return rsaResource ;
    }

    /**
     *
     * @param toDecrypt
     * @param rsaResource
     * @param zip
     * @return
     */
    public static String decryptByPublicAes(String toDecrypt , RSAResource rsaResource,Integer zip) throws Exception {

        // 协议体非空校验
        if (StringUtils.isEmpty(toDecrypt)) {
            throw new EncryptException("to decrypt is null");
        }
        // 协议体转换字节数组 base64反编码
        byte[] toDecryptBytes = hexStringToBytes(toDecrypt);
        if (toDecryptBytes == null) {
            throw new EncryptException("toDecrypt hex to bytes is null");
        }

        // 协议体获取AesKey 字节数组长度值
        byte[] aesKeyLenBytes = new byte[6];
        System.arraycopy(toDecryptBytes, 0, aesKeyLenBytes, 0, 6);
        int aesKeyLen = Integer.parseInt(new String(aesKeyLenBytes).trim());
//        int aesKeyLen = Bytes.toInt(aesKeyLenBytes);
        if (aesKeyLen < 0 || aesKeyLen >= toDecryptBytes.length) {
            throw new EncryptException("aes key length wrong");
        }

        // 协议体获取aesKey内容字节数组
        byte[] aesKeyBytes = new byte[aesKeyLen];
        System.arraycopy(toDecryptBytes, 6, aesKeyBytes, 0, aesKeyLen);
        String aesKey = null;
        // Rsa非对称协议解密AesKey
        aesKeyBytes = RsaAesUtils.rsaDecodeByPublicKey(aesKeyBytes,rsaResource);
        if (aesKeyBytes != null && aesKeyBytes.length > 0){
            aesKey = new String(aesKeyBytes, Charset.forName("UTF-8"));
        }



        // 协议体获取加密内容字节数组长度值
        byte[] dataLenBytes = new byte[6];
        System.arraycopy(toDecryptBytes, 6 + aesKeyLen, dataLenBytes, 0, 6);
        int dataLen = Integer.parseInt(new String(dataLenBytes).trim());
        if (dataLen < 0 || dataLen >= toDecryptBytes.length) {
            throw new EncryptException("toDecryptBytes length wrong");
        }

        // 获取协议体内容字节数组
        byte[] data = new byte[dataLen];
        System.arraycopy(toDecryptBytes, 12 + aesKeyLen, data, 0, dataLen);
        String toDecryptStr = new String(data);
        // AES解密内容
        String decryptStr = AESUtils.aesDecrypt(toDecryptStr,aesKey);
        byte[] decryptedBytes = decryptStr.getBytes("UTF-8");
        // 解密消息体字节数组解压缩
        if (zip != null && zip == 1) {
            decryptedBytes = ZipUtils.unzip(decryptedBytes);
        }

        if (decryptedBytes == null || decryptedBytes.length < 1) {
            throw new EncryptException("aes decryptedBytes is null ");
        }

        String decryptStrUtf8 = new String(decryptedBytes, Charset.forName("UTF-8")).trim();
        return decryptStrUtf8;
    }


    /**
     * bytes to hexString
     * @param
     * @throws Throwable
     */
    public static  String bytesToHexString( byte[] b) {
        String a = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            a = a+hex;
        }
        return a;
    }

    public static byte[] hexStringToBytes(String hexString){
        if (StringUtils.isEmpty(hexString) || "".equals(hexString)||hexString ==null){
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length()/2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0;i<length;i++){
            int pos = i*2;
            d[i] = (byte) (charToByte(hexChars[pos])<<4| charToByte(hexChars[pos+1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
    
    public static RsaVO genRsaKeyPairs(){
        BigInteger[] bigIntegers = new BigInteger[3];
        try {
            return RsaUtil.genRSAKeyPair();
        } catch (Exception e) {
            logger.info("【生成公私钥对】异常！",e);
            throw new EncryptException("generate rsa pairs ex");
        }
    }

    private static String lengthPadWithZero(int length){
        StringBuilder sb = new StringBuilder();
        sb.append(length);
        // 前端填充0
        StringBuilder prefixZero = new StringBuilder();
        if (sb.length()<6){
            for (int i=0;i<6-sb.length();i++){
                prefixZero.append(0);
            }
            prefixZero.append(sb);
        }else if (sb.length()>6) {
            return null;
        } else{
            prefixZero = sb;
        }
        return String.valueOf(prefixZero);
    }


    /**
     * 私钥服务器端根据maxus_jwt 解密消息
     * @param encryptMsg
     * @param maxusJwt
     * @return
     */
    public static String decryptByPrivateKeyAes(String encryptMsg,String maxusJwt)  {
        RsaVO rsaVO = JwtTools.parsePayload(maxusJwt, OverseaClaims.class).getRsaVO();
        RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVO);
        try {
            String s = RsaAesUtils.decryptByPrivateKeyAes(encryptMsg, rsaResource, 0);
            return s;
        } catch (Exception e) {
            log.error("server decryptByPrivateKeyAes msg exception", e.getMessage());
            return null;
        }
    }

    /**
     * 私钥服务器端根据私钥加密消息
     * @param encryptMsg
     * @param maxusJwt
     * @return
     */
    public static String encryptByPrivateAes(String encryptMsg,String maxusJwt)  {
        RsaVO rsaVO = JwtTools.parsePayload(maxusJwt, OverseaClaims.class).getRsaVO();
        RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVO);
        try {
            String s = RsaAesUtils.encryptByPrivateAes(encryptMsg, rsaResource, 0);
            return s;
        } catch (Exception e) {
            log.error("server encryptByPrivateAes msg exception", e.getMessage());
            return null;
        }
    }

    public static String encryptByPublicKeyAes(String toEncrypt, String maxusJwt) throws Exception {
        OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
        RsaVO rsaVO = overseaClaims.getRsaVO();
        RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVO);
        String s = RsaAesUtils.encryptByPublicKeyAes(toEncrypt, rsaResource, 0);
        return s;
    }

    public static String decryptByPublicAes(String encryptMsg,String maxusJwt) throws Exception {
        RsaVO rsaVO = JwtTools.parsePayload(maxusJwt, OverseaClaims.class).getRsaVO();
        RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVO);
        String s = RsaAesUtils.decryptByPublicAes(encryptMsg, rsaResource, 0);
        return s;
    }




}
