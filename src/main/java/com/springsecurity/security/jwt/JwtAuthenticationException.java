package com.springsecurity.security.jwt;

/**
 * @classDesc: 自定义授权异常类
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 08:31:06
 * @version: v1.0
 */
public class JwtAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JwtAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
