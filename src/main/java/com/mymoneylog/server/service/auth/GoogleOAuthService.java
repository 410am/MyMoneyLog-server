package com.mymoneylog.server.service.auth;

import com.mymoneylog.security.jwt.JwtProvider;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier verifier;


    @Value("${jwt.access-expiration}")
    private Duration accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;

    public Map<String, Object> handleGoogleOAuthLogin(String idToken, HttpServletResponse response) {
        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) {
                throw new IllegalArgumentException("유효하지 않은 ID Token");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            String name = (String)payload.get("name");
            String picture = (String)payload.get("picture");
            String email = payload.getEmail();
            String providerId = payload.getSubject();


            // DB에 사용자 없으면 생성
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setNickname(name);
                        newUser.setPicture(picture);
                        newUser.setEmail(email);
                        newUser.setProviderId(providerId);
                        return userRepository.save(newUser);
                    });


            // 토큰 발급
            String accessToken = jwtProvider.createToken(String.valueOf(user.getUserId()), "ROLE_USER", accessExpiration); 
            String refreshToken = jwtProvider.createToken(String.valueOf(user.getUserId()), "ROLE_USER", refreshExpiration);

            // // RefreshToken 쿠키로 전달
            // Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            // refreshCookie.setHttpOnly(true);
            // refreshCookie.setSecure(true);
            // refreshCookie.setPath("/");
            // refreshCookie.setMaxAge((int) (7 * 24 * 60 * 60));

            
            // response.addCookie(refreshCookie);


                        // ✅ ResponseCookie로 refreshToken 설정 (SameSite=None; Secure; HttpOnly)
                        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                        .httpOnly(true)
                        .secure(true)               // HTTPS에서만 전송
                        .sameSite("None")            // 크로스사이트 쿠키 허용
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60)   // 7일 (초 단위)
                        .build();
    
                response.addHeader("Set-Cookie", refreshCookie.toString());
    
            //결과 반환
            Map<String, Object> result = new HashMap<>();
            result.put("jwt", accessToken);
            result.put("email", user.getEmail());
            result.put("userId", user.getUserId());
            result.put("nickname", user.getNickname());
            result.put("picture", user.getPicture());
            System.out.println("✅ result map = " + result);
    
            return result;


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google 로그인 처리 실패", e);
            
        }
    }
}
