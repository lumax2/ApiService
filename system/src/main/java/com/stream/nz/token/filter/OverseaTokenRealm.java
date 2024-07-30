package com.stream.nz.token.filter;

import com.stream.nz.token.JwtTools;
import com.stream.nz.token.exception.AuthException;
import com.stream.nz.token.model.dto.OverseaClaims;
import com.stream.nz.token.model.dto.OverseaToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@Slf4j
public class OverseaTokenRealm extends AuthorizingRealm {

    public static final String OVERSEA_TOKEN_REALM = "OverseaTokenRealm";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OverseaToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        OverseaClaims claims = (OverseaClaims) authenticationToken.getPrincipal();
        String token = (String) authenticationToken.getCredentials();
        if (claims == null) {
            throw new AuthException("Login Claim is null!");
        }
        JwtTools.verifyJwt(token, JwtTools.getSigner(claims.getServiceCode()));
        return new SimpleAuthenticationInfo(claims, token, OVERSEA_TOKEN_REALM);
    }
}
