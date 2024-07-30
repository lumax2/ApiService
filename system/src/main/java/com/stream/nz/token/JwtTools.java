package com.stream.nz.token;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.token.exception.AuthException;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * @author chen yue
 * @date 2022-07-26 08:01:00
 */
@Slf4j
public class JwtTools {
    private static final String FILE_NAME = "rsaKey/rsaKey.pub";
    private static final PublicKey PUB_KEY = getXyxPubKey();
    private static final int EXPIRE_HOUR = 24;

    private JwtTools() {}

    /**
     * 解析token，并获取payload，反序列化为指定类型对象
     *
     * @param token token
     * @param clz 指定类型
     * @return payload 对象
     * @param <T> 类型泛型
     */
    public static <T>T parsePayload(String token, Class<T> clz) {
        return JWT.of(token).getPayload().getClaimsJson().toBean(clz);
    }

    /**
     * 根据payload和指定签名秘钥生成token
     *
     * @param payload 载荷
     * @param key 签名秘钥
     * @return token
     */
    public static String create(JSONObject payload, String key) {
        return create(payload, key, EXPIRE_HOUR);
    }

    /**
     * 根据payload、指定签名秘钥、指定过期时间生成token
     *
     * @param payload 载荷
     * @param key 签名秘钥
     * @param expireMinute 过期分钟
     * @return token
     */
    public static String create(JSONObject payload, String key, int expireMinute) {
        Date now = DateUtil.date(), expire = DateUtil.offsetMinute(now, expireMinute);
        return JWT.create().addPayloads(payload).setIssuedAt(now).setExpiresAt(expire).sign(getSigner(key));
    }

    /**
     * 根据指定的秘钥通过hs256算法获取签名
     *
     * @return 签名
     */
    public static JWTSigner getXyxSigner() {
        return JWTSignerUtil.rs256(PUB_KEY);
    }

    /**
     * 根据指定的秘钥通过hs256算法获取签名
     *
     * @param key 秘钥key
     * @return 签名
     */
    public static JWTSigner getSigner(String key) {
        return JWTSignerUtil.hs256(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验JWT token字符串，内容包括：
     * <ul>
     *     <li>token完整性</li>
     *     <li>算法</li>
     *     <li>是否过期</li>
     * </ul>
     *
     * @param token token
     * @throws AuthException token为空时抛出
     * @throws JWTException 校验不通过抛出
     */
    public static void verifyJwt(String token, JWTSigner signer) throws AuthException {
        if (StrUtil.isEmpty(token)) {
            log.error("token missing");
            throw new AuthException("token missing");
        }
        try{
            JWTValidator.of(token).validateAlgorithm(signer).validateDate();
        }catch (Exception e){
            throw new AuthException("verify jwt wrong");
        }

    }

    /**
     * 获取新营销公钥
     *
     * @return 新营销公钥
     */
    private static PublicKey getXyxPubKey() {
        byte[] keyBytes;
        try(InputStream resourceAsStream = JwtTools.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (resourceAsStream == null) {
                throw new AuthException(String.format("文件缺失: %s", FILE_NAME));
            }

            try(DataInputStream dis = new DataInputStream(resourceAsStream)) {
                keyBytes = new byte[resourceAsStream.available()];
                dis.readFully(keyBytes);
            }
        } catch (IOException e) {
            throw new AuthException(String.format("获取文件失败: %s", FILE_NAME), e);
        }

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthException("秘钥解析失败", e);
        }
    }
}
