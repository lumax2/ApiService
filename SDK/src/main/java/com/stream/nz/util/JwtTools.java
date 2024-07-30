package com.stream.nz.util;

import cn.hutool.jwt.JWT;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chen yue
 * @date 2022-07-26 08:01:00
 */
@Slf4j
public class JwtTools {

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

}
