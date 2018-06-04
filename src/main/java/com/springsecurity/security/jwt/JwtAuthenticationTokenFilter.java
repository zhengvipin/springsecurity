package com.springsecurity.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @classDesc: JWT 过滤器
 * @author: Vipin Zheng
 * @createDate: 2018-05-13 22:21:46
 * @version: v1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.token.head}")
    private String tokenHead;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 获得请求头Authentication的内容
        String requestToken = request.getHeader(jwtHeader);
        if (requestToken != null && requestToken.startsWith(tokenHead)) {
            // 获得令牌(即requestToken中Bearer 后的部分)
            String authToken = requestToken.substring(tokenHead.length());
            // 从令牌中获得用户名
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名获得安全用户对象
                UserDetails user = userDetailsService.loadUserByUsername(username);
                // 验证令牌
                if (jwtTokenUtil.validateToken(authToken, user)) {
                    // 生成通过认证
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                    // 加载额外数据源参数，如验证码等
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将权限写入本次会话
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

// 客户端将token封装在请求头中，格式为（Bearer后加空格）：Authorization：Bearer +token
// SecurityContextHolder.getContext().getAuthentication() == null 表示用户尚未登录
