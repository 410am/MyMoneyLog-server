package com.mymoneylog.server.controller.auth;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.security.jwt.JwtProvider;

@RestController
@RequestMapping("/token")
public class RefreshTokenController {

    private final JwtProvider jwtProvider;

    public RefreshTokenController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
      
    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration; 

    // @PostMapping("/refresh")
    // public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
      

    //     String refreshToken = request.get("refreshToken");

    //     if (refreshToken == null || refreshToken.isBlank()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리프레시 토큰이 없습니다.");
    //     }
 
    //     // 🔹 1. 리프레시 토큰 검증
    //     if (!jwtProvider.validateToken(refreshToken)) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    //     }

    //     // 사용자 정보 추출
    //     String userId = jwtProvider.getSubject(refreshToken);
    //     String role = jwtProvider.getRole(refreshToken); 

    //     // 🔹 2. 기존 정보로 새로운 액세스 토큰 재발급
    //     String newAccessToken = jwtProvider.createToken(userId, role, refreshExpiration);
    //     System.out.println("🔄 RefreshToken 검증 후 새 AccessToken 발급 완료");

    //     return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    // }

    @PostMapping("/refresh")
public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리프레시 토큰이 없습니다.");
    }

    if (!jwtProvider.validateToken(refreshToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    String userId = jwtProvider.getSubject(refreshToken);
    String role = jwtProvider.getRole(refreshToken);
    String newAccessToken = jwtProvider.createToken(userId, role, refreshExpiration);

    System.out.println("🔄 RefreshToken 검증 후 새 AccessToken 발급 완료");

    return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
}
}
