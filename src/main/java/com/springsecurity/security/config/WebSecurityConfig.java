package com.springsecurity.security.config;

import com.springsecurity.security.jwt.JwtAccessDeniedHandler;
import com.springsecurity.security.jwt.JwtAuthenticationEntryPoint;
import com.springsecurity.security.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


/**
 * @classDesc: 安全配置类
 * @author: Vipin Zheng
 * @createDate: 2018-05-11 14:53:06
 * @version: v1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Resource
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Bean // 配置BCrypt密码编译器
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 配置授权认证管理器，用于登录认证
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 1.配置认证管理器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // 2.设置http拦截规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 由于使用的是JWT，我们不需要csrf
                .csrf().disable()
                // 跨域
                .cors().and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 除已经配置忽略验证外的所有请求全部需要鉴权认证
                .authorizeRequests().anyRequest().authenticated();

        // 禁用缓存
        http.headers().cacheControl(); // http.requestCache().requestCache(new NullRequestCache());
        // 允许加载同源frame页面
        http.headers().frameOptions().sameOrigin();

        // 集成JWT
        // 1.在 UsernamePasswordAuthenticationFilter 前添加 JWT过滤器
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 2.未授权处理
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        // 3.权限不足处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    // 3.设置web放行规则
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                // 1.对于获取token的rest api要允许匿名访问
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        "/api" + authenticationPath
                )
                .and()
                // 2.允许对于网站静态资源的无授权访问
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                );
    }
}
