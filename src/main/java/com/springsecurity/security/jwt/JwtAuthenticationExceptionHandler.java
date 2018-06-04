package com.springsecurity.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @classDesc: 自定义异常处理类
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 08:32:17
 * @version: v1.0
 */
@ControllerAdvice //  注解定义全局异常处理类
public class JwtAuthenticationExceptionHandler {
    @ExceptionHandler({AuthenticationException.class}) // 注解声明异常处理方法
    private ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}

