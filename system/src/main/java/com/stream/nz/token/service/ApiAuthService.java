package com.stream.nz.token.service;

import com.stream.nz.config.exception.AdminException;
import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.encrypt.service.RsaService;
import com.stream.nz.token.JwtTools;
import com.stream.nz.token.model.dto.ApiAuthDto;
import com.stream.nz.token.model.dto.OverseaClaims;
import com.stream.nz.token.model.po.TmOverseaService;
import com.stream.nz.token.support.ServiceSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiAuthService {

    private final ServiceSupport serviceSupport;

    private Long SERVICE_EFFECTIVE_TIME = 3L;

    public static final String SERVICE_TOKEN_PREFIX = "SERVICE_JWT_";

    private final RedisTemplate redisTemplate;

    private final RsaService rsaService;


    public String registerApiToken(ApiAuthDto apiAuthDto) {

        TmOverseaService tm = serviceSupport.queryByServiceCode(apiAuthDto.getServiceCode());
        if (null == tm){
            throw new AdminException("serviceCode is wrong");
        }
        if (!apiAuthDto.getServiceSecret().equals(tm.getServiceSecret())){
            throw new AdminException("serviceSecret is wrong");
        }
        RsaVO rsaVO = rsaService.createRsaModuleViaServiceCode(tm.getServiceCode());
        //封装claims, 生成jwtToken
        OverseaClaims claims = new OverseaClaims(tm,rsaVO);
        //生成tokenId
        String token = JwtTools.create(claims.toJSONObject(), claims.getServiceCode());
        //tokenId存入redis，有效期3分钟
        String redisKey = SERVICE_TOKEN_PREFIX + UUID.randomUUID();
        redisTemplate.opsForValue().set(redisKey, tm.getServiceCode(), SERVICE_EFFECTIVE_TIME, TimeUnit.MINUTES);

        return token;

    }

}
