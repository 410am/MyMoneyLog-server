package com.mymoneylog.server.service.auth;

import com.mymoneylog.security.jwt.JwtProvider;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    public Map<String, String> handleGoogleOAuthLogin(String idToken, HttpServletResponse response) {
        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) {
                throw new IllegalArgumentException("유효하지 않은 ID Token");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            String providerId = payload.getSubject();

            

            // DB에 사용자 없으면 생성
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setProviderId(providerId);
                        return userRepository.save(newUser);
                    });


            // 토큰 발급
            String accessToken = jwtProvider.createToken(String.valueOf(user.getUserId()), "ROLE_USER", 1000 * 60); // 15분
            String refreshToken = jwtProvider.createToken(String.valueOf(user.getUserId()), "ROLE_USER", 1000L * 60 * 60 * 24 * 7); // 7일

            // RefreshToken 쿠키로 전달
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge((int) (7 * 24 * 60 * 60));
            response.addCookie(refreshCookie);


    
    
            Map<String, String> result = new HashMap<>();
            result.put("jwt", accessToken);
            result.put("email", user.getEmail());
            System.out.println("✅ result map = " + result);
    
            return result;


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google 로그인 처리 실패", e);
            
        }
    }
}
