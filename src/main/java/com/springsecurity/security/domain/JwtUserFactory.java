package com.springsecurity.security.domain;

import com.springsecurity.domain.Role;
import com.springsecurity.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @classDesc: 用户工厂
 * @author: Vipin Zheng
 * @createDate: 2018-05-11 10:52:36
 * @version: v1.0
 */
public class JwtUserFactory {

    /**
     * 关联用户和安全用户
     *
     * @param user 用户
     * @return 安全用户
     */
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEnabled() == 1,
                user.getLastPasswordResetDate(),
                user.getLoginDate(),
                roleToAuthority(user.getRoles())
        );
    }

    /**
     * 关联角色和权限
     *
     * @param roles 角色集合
     * @return 权限集合
     */
    private static List<GrantedAuthority> roleToAuthority(List<Role> roles) {
        return roles.stream()
                .map(e -> new SimpleGrantedAuthority(e.getName()))
                .collect(Collectors.toList());
    }
}
