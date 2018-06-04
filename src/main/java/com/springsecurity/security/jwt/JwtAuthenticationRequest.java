package com.springsecurity.security.jwt;

import java.io.Serializable;

/**
 * @classDesc: 授权请求类
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 13:28:15
 * @version: v1.0
 */
public class JwtAuthenticationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
