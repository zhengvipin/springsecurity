package com.springsecurity.security.jwt;

import java.io.Serializable;

/**
 * @classDesc: 授权响应类
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 13:29:20
 * @version: v1.0
 */
public class JwtAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
