package com.mymoneylog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 🔹 1. 로그인한 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 🔹 2. 사용자 정보에서 이메일, 이름 추출
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");


        // 예: 사용자 ID, 권한 정보를 클레임으로 추출
        String userId = oAuth2User.getAttribute("sub");      // 구글 고유 ID
        String role = "ROLE_USER";                           // 예시로 고정

        // JwtProvider를 사용해서 토큰 생성
        String token = jwtProvider.createToken(userId, role);


        // 🔹 4. 프론트로 리디렉션 + 토큰 전달 (쿼리 파라미터로 전달)
        // response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + token);
        response.getWriter().write("로그인 성공! 발급된 JWT: " + token);
    }
}
