package com.stream.nz.token.model.dto;

import org.apache.shiro.authc.AuthenticationToken;

public class OverseaToken implements AuthenticationToken {

    public static final int TYPE_SERVICE_JWT_TOKEN = 2;

    private final OverseaClaims claims;

    private final String token;

    public OverseaToken(OverseaClaims claims, String token) {
        this.claims = claims;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return claims;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}