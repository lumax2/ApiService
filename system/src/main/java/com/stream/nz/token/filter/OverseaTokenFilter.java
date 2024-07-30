package com.stream.nz.token.filter;

import com.stream.nz.token.JwtTools;
import com.stream.nz.token.model.dto.OverseaClaims;
import com.stream.nz.token.model.dto.OverseaToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Locale;

@Slf4j
public class OverseaTokenFilter extends AbstractShiroFilter {

    private static final String JWT = "jwt".toLowerCase(Locale.ROOT);

    @Override
    protected void handleDisable(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        response403(response, "contact admin");
    }

    @Override
    protected void handleFail(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        response401(response, "please login");
    }

    @Override
    protected AuthenticationToken doCreateTokenObj(String token) {
        return new OverseaToken(JwtTools.parsePayload(token, OverseaClaims.class), token);
    }

    @Override
    protected String getAuthHeader() {
        return JWT;
    }

    @Override
    protected String getAuthCookieName() {
        return JWT;
    }
}
