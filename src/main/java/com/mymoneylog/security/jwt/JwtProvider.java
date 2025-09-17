package com.mymoneylog.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        // secretKey를 바이트 배열로 변환해서 HMAC-SHA256 키 생성
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성
     */
    public String createToken(String userId, String role, Duration expiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * JWT 토큰 검증 및 Claims 반환
     */
    // public boolean validateToken(String token) {
    //     try {
    
    //         return Jwts.parserBuilder()
    //         .setSigningKey(key)
    //         .setAllowedClockSkewSeconds(5) // 시간 오차 허용
    //         .build()
    //         .parseClaimsJws(token).getBody() != null;
         
    //     } catch (ExpiredJwtException e) {
    //         // ⛔ 토큰이 만료된 경우
    //         log.warn("JWT 만료: {}", e.getMessage());
    //         // todo : custom exception 처리 (만료만 따로 프론트에 알려주기)
    //         // return false;
    //         throw e;
    //     } catch (Exception e) {
    //         log.warn("JWT 오류: {}", e.getMessage());
    //         // return false;
    //         throw e;
    //     }
    // }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(5)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료: {}", e.getMessage());
            return false; // 🔴 여기서 false 반환
        } catch (Exception e) {
            log.warn("JWT 오류: {}", e.getMessage());
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .setAllowedClockSkewSeconds(5) // 테스트용 오차 허용
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    public String getSubject(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
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
            .setSigningKey(key)
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
