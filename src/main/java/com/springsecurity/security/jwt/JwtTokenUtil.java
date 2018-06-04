package com.springsecurity.security.jwt;

import com.springsecurity.security.domain.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @classDesc: JWT 工具类
 * @author: Vipin Zheng
 * @createDate: 2018-05-13 21:30:37
 * @version: v1.0
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.token.expiration}")
    private Long expiration;

    @Value("${jwt.token.secret}")
    private String secret;

    /**
     * 生成令牌
     *
     * @param userDetails 安全用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        // 生成令牌
        return Jwts.builder()
                .setClaims(claims)                            // 自定义数据声明
                .setSubject(userDetails.getUsername())        // 主体
                .setIssuedAt(createdDate)                    // 签发日期
                .setExpiration(expirationDate)                // 过期日期
                .signWith(SignatureAlgorithm.HS512, secret)   // 加密算法 | 秘钥
                .compact();   // 压缩令牌
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 安全用户
     * @return 是否通过验证
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        String username = getUsernameFromToken(token);
        Date createDate = getCreatedDateFromToken(token);
        System.out.println(!isCreatedBeforeLastPasswordResetDate(createDate, user.getLastPasswordResetDate()));
        return username.equals(user.getUsername())
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordResetDate(createDate, user.getLastPasswordResetDate());
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 判断令牌能否被刷新
     *
     * @param token                令牌
     * @param lastPasswordRestDate 上次改密时间
     * @return 能否被刷新
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordRestDate) {
        return !isCreatedBeforeLastPasswordResetDate(getCreatedDateFromToken(token), lastPasswordRestDate)
                && (!isTokenExpired(token) || isIgnoreExpiration(token));
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        return getExpirationFromToken(token).before(new Date());
    }

    /**
     * 判断签发日期是否早于上次改密日期
     *
     * @param createdDate          签发日期
     * @param lastPasswordRestDate 上次改密日期
     * @return 是否早于
     */
    public Boolean isCreatedBeforeLastPasswordResetDate(Date createdDate, Date lastPasswordRestDate) {
        return (lastPasswordRestDate != null && createdDate.before(lastPasswordRestDate));
    }

    /**
     * 判断是否要忽略过期时间
     *
     * @param token 令牌
     * @return 是否忽略
     */
    public Boolean isIgnoreExpiration(String token) {
        return false;
    }

    /**
     * 从令牌中获得用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从令牌中获得签发日期
     *
     * @param token 令牌
     * @return 签发日期
     */
    public Date getCreatedDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * 从令牌中获得过期日期
     *
     * @param token 令牌
     * @return 过期日期
     */
    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从令牌中获得具体的数据声明
     *
     * @param token 令牌
     * @param func  函数式接口
     * @param <T>   具体的数据声明类型
     * @return 具体的数据声明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> func) {
        return func.apply(getAllClaimsFromToken(token));
    }

    /**
     * 从令牌中获得数据声明对象
     *
     * @param token 令牌
     * @return 数据声明对象
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()  // 解压令牌
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
