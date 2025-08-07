package com.mymoneylog.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * JWT 토큰 생성
     */
    public String createToken(String userId, String role, long expiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    /**
     * JWT 토큰 검증 및 Claims 반환
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // ⛔ 토큰이 만료된 경우
            log.warn("JWT 만료: {}", e.getMessage());
            // todo : custom exception 처리 (만료만 따로 프론트에 알려주기)
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getSubject(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }


    /**
     * JWT에서 역할 꺼내기
     */
    public String getRole(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }


    // /**
    //  * JWT에서 사용자 ID 꺼내기
    //  */
    // public String getUserId(String token) {
    //     return validateToken(token).getSubject(); // subject = userId
    // }



}
