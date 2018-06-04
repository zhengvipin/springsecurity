package com.springsecurity.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.springsecurity.security.domain.JwtUser;
import com.springsecurity.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


/**
 * @classDesc: 用户模型控制器
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 21:21:20
 * @version: v1.0
 */
@RestController
@RequestMapping("/api")
public class UserRestController {

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.token.head}")
    private String tokenHeader;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        // 1.从请求头中获得令牌
        String token = request.getHeader(jwtHeader).substring(tokenHeader.length());
        // 2.从令牌中获得用户信息
        String username = jwtTokenUtil.getUsernameFromToken(token);
        // 3.将用户信息返回页面
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }
}
