package com.stream.nz.token.filter;

import cn.hutool.core.util.StrUtil;
import com.stream.nz.token.exception.TokenDisabledException;
import com.stream.nz.token.exception.TokenFailedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
public abstract class AbstractShiroFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return doCreateToken(request) != null;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        return doCreateToken(request);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Throwable cause = e.getCause();
        if (cause instanceof TokenDisabledException) {
            handleDisable(token, e, request, response);
            return false;
        }

        if (cause instanceof TokenFailedException) {
            handleFail(token, e, request, response);
            return false;
        }

        return false;
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        response401(response, "has no token");
        return false;
    }

    /**
     * 处理禁用状态
     *
     * @param token 认证信息
     * @param e 报错信息
     * @param request request
     * @param response response
     */
    protected abstract void handleDisable(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response);

    /**
     * 处理认证失败状态
     *
     * @param token 认证信息
     * @param e 报错信息
     * @param request request
     * @param response response
     */
    protected abstract void handleFail(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response);

    /**
     * 创建token对象
     *
     * @param token token内容
     * @return 创建后的token对象
     */
    protected abstract AuthenticationToken doCreateTokenObj(String token);

    /**
     * 获取认证header名
     *
     * @return 认证header名
     */
    protected abstract String getAuthHeader();

    /**
     * 获取认证cookie名
     *
     * @return 认证cookie名
     */
    protected abstract String getAuthCookieName();

    /**
     * 响应401
     *
     * @param response 响应对象
     * @param msg 信息
     */
    protected void response401(ServletResponse response, String msg) {
        doResponse(response, 401, msg);
    }

    /**
     * 响应403
     *
     * @param response 响应对象
     * @param msg 信息
     */
    protected void response403(ServletResponse response, String msg) {
        doResponse(response, 403, msg);
    }

    /**
     * 响应信息的脚手架代码
     *
     * @param response 响应实体
     * @param code 响应码
     * @param msg 消息
     */
    private void doResponse(ServletResponse response, int code, String msg) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(code);
        httpResponse.setHeader("Authorization", msg);
    }

    /**
     * 获取认证 token
     *
     * @param httpServletRequest request
     * @return token
     */
    private String getToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(getAuthHeader());
        if (token != null) {
            return token;
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(getAuthCookieName(), cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        return token;
    }

    /**
     * 创建token对象的脚手架代码
     *
     * @param request 请求对象
     * @return 生成的token对象
     */
    private AuthenticationToken doCreateToken(ServletRequest request) {
        String token;
        try {
          token = getToken((HttpServletRequest) request);
        } catch (Exception e) {
            log.warn("dms token 获取时遇到问题失败：", e);
            return null;
        }

        if (StrUtil.isEmpty(token)) {
            return null;
        }

        try {
            return doCreateTokenObj(token);
        } catch (Exception e) {
            log.warn("dms token对象创建过程遇到异常失败：", e);
            return null;
        }
    }
}
