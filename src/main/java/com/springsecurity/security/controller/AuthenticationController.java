package com.springsecurity.security.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.springsecurity.security.domain.JwtUser;
import com.springsecurity.security.jwt.JwtAuthenticationException;
import com.springsecurity.security.jwt.JwtAuthenticationRequest;
import com.springsecurity.security.jwt.JwtAuthenticationResponse;
import com.springsecurity.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * @classDesc: 授权控制器 —— 不需要通过JwtAuthenticationTokenFilter鉴权认证
 * @author: Vipin Zheng
 * @createDate: 2018-06-04 08:34:04
 * @version: v1.0
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.token.head}")
    private String tokenHead;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 创建授权令牌 (登录)
     *
     * @param request 授权请求类
     * @return 返回页面的数据
     * @throws JwtAuthenticationException 授权异常
     */
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest request) throws JwtAuthenticationException {
        // 1.验证用户登录信息
        try {
            // 1.1 使用name和password封装成为未认证令牌 unauthorizedToken
            Authentication unauthorizedToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            // 1.2 将token传递给Authentication进行验证
            authenticationManager.authenticate(unauthorizedToken);
        }
        // 1.3 按照顺序抛出以下三个异常
        catch (DisabledException e) {
            throw new JwtAuthenticationException("User is disabled!", e);
        } catch (LockedException e) {
            throw new JwtAuthenticationException("User is locked!", e);
        } catch (BadCredentialsException e) {
            throw new JwtAuthenticationException("Bad credentials!", e);
        }
        // 2.获得已认证令牌 token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        // 3.返回令牌
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /**
     * 刷新令牌
     *
     * @param request 授权请求
     * @return 返回页面数据
     */
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        // 1.从请求头中获取令牌
        String authToken = request.getHeader(jwtHeader);
        String token = authToken.substring(tokenHead.length());
        // 2.根据令牌获取用户信息
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        // 3.刷新令牌
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
