package com.mymoneylog.server.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.security.jwt.JwtProvider;
import com.mymoneylog.server.service.auth.GoogleOAuthService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtProvider jwtProvider;
    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String idToken = request.get("idToken");
        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body("idToken이 없습니다");
        }
        String email = request.get("email");

        Map<String, Object> tokens = googleOAuthService.handleGoogleOAuthLogin(idToken, response);

        Map<String, Object> body = new HashMap<>();
        body.put("data", tokens);
    
        return ResponseEntity.ok(body);
    }

    // @Value("${jwt.access-expiration}")
    // private long accessExpiration; 

    // @Value("${jwt.refresh-expiration}")
    // private long refreshExpiration; 

    @Value("${jwt.access-expiration}")
    private Duration accessExpiration;
    
    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;
    

//     @PostMapping("/refresh")
// public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
//     if (refreshToken == null || refreshToken.isBlank()) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리프레시 토큰이 없습니다.");
//     }

//     if (!jwtProvider.validateToken(refreshToken)) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
//     }

//     String userId = jwtProvider.getSubject(refreshToken);
//     String role = jwtProvider.getRole(refreshToken);
//     String newAccessToken = jwtProvider.createToken(userId, role, accessExpiration);

//     System.out.println("🔄 RefreshToken 검증 후 새 AccessToken 발급 완료");

//     // return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

//     return ResponseEntity
//         .ok()
//         .header("Cache-Control", "no-store")
//         .body(Map.of("accessToken", newAccessToken));

//         // return ResponseEntity.ok(Map.of("data", tokens, "email", email));  // 프론트는 res.data.data.jwt 로 받을 수 있음
//     }

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
    String newAccessToken = jwtProvider.createToken(userId, role, accessExpiration);

    System.out.println("🔄 RefreshToken 검증 후 새 AccessToken 발급 완료");

    return ResponseEntity
        .ok()
        .header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
        .header("Pragma", "no-cache")
        .header("Expires", "0")
        .body(Map.of("accessToken", newAccessToken));
}


@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0) // 바로 만료
            .build();

    response.addHeader("Set-Cookie", cookie.toString());
    return ResponseEntity.ok("로그아웃 완료");
}
}

