package com.springsecurity.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @classDesc: 安全用户模型
 * @author: Vipin Zheng
 * @createDate: 2018-05-11 10:39:28
 * @version: v1.0
 */
public class JwtUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Date lastPasswordResetDate;
    private Date loginDate;
    // 关联安全用户和权限
    private List<GrantedAuthority> authorities;

    public JwtUser(Long id, String username, String password, boolean enabled, Date lastPasswordResetDate, Date loginDate, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.loginDate = loginDate;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    @JsonIgnore
    public Date getLoginDate() {
        return loginDate;
    }
}
