package com.stream.nz.encrypt.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.stream.nz.encrypt.exception.EncryptException;
import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.encrypt.model.ToDecryptDto;
import com.stream.nz.encrypt.model.ToEncryptDto;
import com.stream.nz.encrypt.util.RSAResource;
import com.stream.nz.encrypt.util.RsaAesUtils;
import com.stream.nz.token.JwtTools;
import com.stream.nz.token.model.dto.OverseaClaims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RsaServiceImpl implements RsaService {
    private static final Logger logger = LoggerFactory.getLogger( RsaServiceImpl.class);

    private final StringRedisTemplate stringRedisTemplate;

    public static final String RSA_POOL_REDIS_CACHE = "RSA:POOL:";

    public Integer rsaKeyPairsSize = 100000;



    @Override
    public RsaVO createRsaModuleViaServiceCode(String serviceCode) {
        try {
            RsaVO rsaVo = null;
            // 查询旧有公钥 更新公钥过期时间
            String encryptKey = String.valueOf(new StringBuilder().append("auth:encrypt:").append(serviceCode) );
            String rsaVoStr = stringRedisTemplate.opsForValue().get(encryptKey);
            if (StringUtils.isNotEmpty(rsaVoStr)){
                try{
                    rsaVo = JSONObject.parseObject(rsaVoStr,RsaVO.class);
                    rsaVo.setGainCount(rsaVo.getGainCount()+1);
                    stringRedisTemplate.opsForValue().set(encryptKey, JSON.toJSONString( rsaVo));
                    return rsaVo;
                }catch (Throwable e){
                    logger.error("【解析旧有公私钥对】异常！",e);
                    throw new EncryptException("");
                }
            }else{
                // 共公私钥池中随机拿去公私钥
                int key = (int)(Math.random()*rsaKeyPairsSize);
                StringBuilder sb = new StringBuilder().append(RSA_POOL_REDIS_CACHE).append(key);
                String rsaVoStrFromPool = stringRedisTemplate.opsForValue().get(String.valueOf(sb));
                if (StringUtils.isNotEmpty(rsaVoStrFromPool)){
                    rsaVo =  JSONObject.parseObject(rsaVoStr,RsaVO.class);
                }
                if (rsaVo==null || StringUtils.isEmpty(rsaVoStr)){
                    // 生成新rsa公私钥对
                    rsaVo = RsaAesUtils.genRsaKeyPairs();
                }
                // Redis存储公私钥
                rsaVo.setGainCount(1);
                stringRedisTemplate.opsForValue().set(encryptKey, JSON.toJSONString( rsaVo));
                return rsaVo;
            }
        }catch ( Throwable e ){
            logger.error( "【获取RSA公钥】异常！",e );
            throw new EncryptException("createRsaModule exception! ");
        }finally {

        }
    }


    @Override
    public RsaVO createRsaModule(String maxusJwt) {
        OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
        String serviceCode = overseaClaims.getServiceCode();
        return createRsaModuleViaServiceCode(serviceCode);
    }

    @Override
    public Boolean disableRsa(String maxusJwt) {
        try{
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            String encryptKey = String.valueOf(new StringBuilder().append("auth:encrypt:").append(overseaClaims.getServiceCode()));
            stringRedisTemplate.delete(encryptKey);
            return true;
        }catch (Throwable e){
            logger.error("【失效RSA公私钥对】异常",e);
            throw new EncryptException("disableRsa exception！");
        }

    }


    @Override
    public String encryptByPublic(ToEncryptDto toEncryptDto) {
        try {
            //根据用户信息获取公私钥对
            String maxusJwt = toEncryptDto.getMaxusJwt();
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            RsaVO rsaVo =getRsaInfoFromRedis(overseaClaims.getServiceCode());

            // 转换用户公私钥对
            RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVo);
            if (null == rsaResource){
                throw new EncryptException("Rsa pair disabled");
            }
            // Rsa+AES 加密消息体
            String encryptBase64Str = RsaAesUtils.encryptByPublicKeyAes(toEncryptDto.getToEncrypt(),rsaResource,0);
            return encryptBase64Str;
        }catch (EncryptException e){
            throw  e;
        }catch (Throwable e){
            logger.error("【公钥加密】异常!",e);
            throw new EncryptException("encryptByPublic exception");
        }
    }

    @Override
    public String decryptByPrivate(ToDecryptDto toDecryptDto) {
        try{
            //根据用户信息获取公私钥对
            String maxusJwt = toDecryptDto.getMaxusJwt();
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            RsaVO rsaVo = getRsaInfoFromRedis(overseaClaims.getServiceCode());

            // 转换用户公私钥对
            RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVo);
            if (null == rsaResource){
                throw new EncryptException("Rsa pair disabled");
            }

            // RSA+AES 解密消息体
            String decryptJsonStr = RsaAesUtils.decryptByPrivateKeyAes(toDecryptDto.getToDecrypt(),rsaResource,0);
            return decryptJsonStr;
        }catch (Throwable e){
            logger.error("【私钥解密】异常！",e);
            throw  new EncryptException("decryptByPrivate exception");
        }
    }

    @Override
    public String decryptByPublic(ToDecryptDto toDecrypt) {
        try{
            //根据用户信息获取公私钥对
            String maxusJwt = toDecrypt.getMaxusJwt();
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            RsaVO rsaVo = getRsaInfoFromRedis(overseaClaims.getServiceCode());

            // 转换用户公私钥对
            RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVo);
            if (null == rsaResource){
                throw new EncryptException("Rsa pair disabled");
            }
            // 公钥+aes解密
            String decryptStr = RsaAesUtils.decryptByPublicAes(toDecrypt.getToDecrypt(),rsaResource,0);
            return decryptStr;
        }catch (Throwable e){
            logger.error("【公钥解密】异常!");
            throw new EncryptException("decryptByPublic exception");
        }
    }

    @Override
    public String encryptByPrivate(ToEncryptDto toEncryptDto) {
        try{
            //根据用户信息获取公私钥对
            String maxusJwt= toEncryptDto.getMaxusJwt();
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            RsaVO rsaVo = getRsaInfoFromRedis(overseaClaims.getServiceCode());

            // 转换用户公私钥对
            RSAResource rsaResource = RsaAesUtils.transferRsaVoToRsaResource(rsaVo);
            if (null == rsaResource){
                throw new EncryptException("Rsa pair disabled");
            }
            // 公钥+aes加密
            String encryptStr = RsaAesUtils.encryptByPrivateAes(toEncryptDto.getToEncrypt(),rsaResource,0);
            return encryptStr;
        }catch (Throwable e){
            logger.error("【私钥加密】异常!");
            throw new EncryptException("encryptByPrivate exception");
        }
    }

    @Override
    public void createRsaKeyPairsInPool() {
        try{
            for (int i = 0;i<rsaKeyPairsSize;i++){
                RsaVO rsaVo = RsaAesUtils.genRsaKeyPairs();
                StringBuilder sb  = new StringBuilder().append(RSA_POOL_REDIS_CACHE).append(i);
                stringRedisTemplate.opsForValue().set(String.valueOf(sb), JSON.toJSONString( rsaVo));
                logger.info("【RSA公私钥对池】成功创建第【{}】条公私钥入池！",(i+1));
            }
            logger.info("【RSA公私钥对】成功创建公私钥对池，共【{}】条！",rsaKeyPairsSize);
        }catch (Throwable e){
            logger.error("【RSA公私钥对】创建公私钥池异常！",e);
            throw new EncryptException("createRsaKeyPairsInPool exception");
        }
    }

    private RsaVO getRsaInfoFromRedis( String userJwtToken) {
        RsaVO rsaVo = null;
        if (StringUtils.isNotEmpty(userJwtToken)) {
            String encryptKey = String.valueOf(new StringBuilder().append("auth:encrypt:").append(userJwtToken));
            String rsaVoStr = stringRedisTemplate.opsForValue().get(encryptKey);
            if (StringUtils.isNotEmpty(rsaVoStr)) {
                rsaVo = JSONObject.parseObject(rsaVoStr , RsaVO.class);
            }
        }
        if (null == rsaVo){
            throw new EncryptException("Rsa pair disabled");
        }
        return rsaVo;
    }


}
