package com.stream.nz.encrypt.service;


import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.encrypt.model.ToDecryptDto;
import com.stream.nz.encrypt.model.ToEncryptDto;

public interface RsaService {


    /**
     * 根据serviceCode创建 RSA 公钥因子 + RSA 模数
     * @param serviceCode
     * @return
     */
    RsaVO createRsaModuleViaServiceCode(String serviceCode);
    /**
     * 根据用户认证信息 获取
     * RSA 公钥因子 + RSA 模数
     * @param maxusJwt
     * @return
     */
    RsaVO createRsaModule(String maxusJwt);

    /**
     * 使用户 公私钥对失效
     * @param userJwtToken
     * @return
     */
    Boolean disableRsa(String userJwtToken);

    /**
     * 公钥加密 测试用
     * @param toEncryptDto
     * @return
     */
    String encryptByPublic(ToEncryptDto toEncryptDto);

    /**
     * 私钥解密
     * @param toDecryptDto
     * @return
     */
    String decryptByPrivate(ToDecryptDto toDecryptDto);

    /**
     * 公钥解密
     * @param toDecryptDtor
     * @return
     */
    String decryptByPublic(ToDecryptDto toDecryptDtor);

    /**
     * 私钥加密
     * @param toEncryptDto
     * @return
     */
    String encryptByPrivate(ToEncryptDto toEncryptDto);

    /**
     * 创建RSA 公私钥对 进入redis池
     */
    void createRsaKeyPairsInPool();
}
