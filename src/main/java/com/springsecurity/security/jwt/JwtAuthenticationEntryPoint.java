package com.springsecurity.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @classDesc: 未授权认证(401)返回值
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 22:29:56
 * @version: v1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -8970718410437077606L;

  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    // This is invoked when user tries to access a secured REST resource without supplying any credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}