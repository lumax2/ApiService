package com.stream.nz.util;


import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * RSA秘钥资源
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSAResource {

    public BigInteger bigPrivateKey;
    public BigInteger bigPublicKey;
    public BigInteger bigModule;
    public int size;

    public RSAResource(String privateHexExponent, String publicHexStrExponent, String modulesHexStr) {
        this.bigPrivateKey = new BigInteger(privateHexExponent, 16);
        this.bigPublicKey = new BigInteger(publicHexStrExponent, 16);
        this.bigModule = new BigInteger(modulesHexStr, 16);
        this.size = 1024;
    }

    public RSAResource(String privateKey, String publicKey, String module, int size) throws Exception {
        if (StrUtil.isEmpty(privateKey) || StrUtil.isEmpty(publicKey) || StrUtil.isEmpty(module))
            throw new Exception("Construct param is error");

        this.bigPrivateKey = new BigInteger(privateKey, 16);
        this.bigPublicKey = new BigInteger(publicKey, 16);
        this.bigModule = new BigInteger(module, 16);
        this.size = size;
    }


    public int getSize() {
        return size;
    }

    public BigInteger getBigPrivateKey() {
        return bigPrivateKey;
    }

    public BigInteger getBigPublicKey() {
        return bigPublicKey;
    }

    public BigInteger getBigModule() {
        return bigModule;
    }
}


