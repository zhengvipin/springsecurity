package com.springsecurity.security.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @classDesc: 权限不足(403)返回值
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 22:59:09
 * @version: v1.0
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
