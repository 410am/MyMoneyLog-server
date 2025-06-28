package com.mymoneylog.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;


@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration; // milliseconds 단위 (예: 3600000 = 1시간)

    /**
     * JWT 토큰 생성
     */
    public String createToken(String userId, String role) {
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
    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * JWT에서 사용자 ID 꺼내기
     */
    public String getUserId(String token) {
        return validateToken(token).getSubject(); // subject = userId
    }

    /**
     * JWT에서 역할 꺼내기
     */
    public String getRole(String token) {
        return validateToken(token).get("role", String.class);
    }

    @PostConstruct
public void init() {
    System.out.println("✅ secretKey = " + secretKey);
    System.out.println("✅ expiration = " + expiration);
}

}
