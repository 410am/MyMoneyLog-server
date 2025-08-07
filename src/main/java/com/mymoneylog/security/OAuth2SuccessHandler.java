package com.mymoneylog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mymoneylog.security.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${jwt.access-expiration}")
    private long accessExpiration; 

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; 


//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Authentication authentication) throws IOException {
//         // 🔹 1. 로그인한 사용자 정보 가져오기
//         OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();


//         // 예: 사용자 ID, 권한 정보를 클레임으로 추출
//         String userId = oAuth2User.getAttribute("sub");      // 구글 고유 ID
//         String role = "ROLE_USER";                           // 예시로 고정

//         // JwtProvider를 사용해서 액세스토큰 생성
//         String accessToken = jwtProvider.createToken(userId, role, accessExpiration);

//         // JwtProvider를 사용해서 리프레시토큰 생성
//         String refreshToken = jwtProvider.createToken(userId, role, refreshExpiration);

//         // 🔸 RefreshToken을 HttpOnly 쿠키로 설정
//         Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//         refreshCookie.setHttpOnly(true);
//         refreshCookie.setSecure(true); // HTTPS 환경에서만
//         refreshCookie.setPath("/");
//         refreshCookie.setMaxAge((int) (refreshExpiration / 1000)); // 초 단위로 설정
//         response.addCookie(refreshCookie);

        
//         // 🔸 AccessToken은 바디로 응답
//         response.setContentType("application/json");
//         response.setCharacterEncoding("UTF-8");
//         response.getWriter().write("{\"accessToken\": \"" + accessToken + "\"}");


//         response.getWriter().write("로그인 성공! 발급된 JWT: " + accessToken);
//         response.getWriter().write("로그인 성공! 발급된 refreshJWT: " + refreshToken);
//     }
// }

@Override
public void onAuthenticationSuccess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) throws IOException {
    // 🔹 로그인한 사용자 정보 가져오기
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    // 🔹 사용자 식별자 및 권한
    String userId = oAuth2User.getAttribute("sub");
    String role = "ROLE_USER";

    // 🔹 JWT 생성
    String accessToken = jwtProvider.createToken(userId, role, accessExpiration);
    String refreshToken = jwtProvider.createToken(userId, role, refreshExpiration);

    // 🔹 RefreshToken을 HttpOnly 쿠키로 설정
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(true); // HTTPS 환경에서만 동작
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge((int) (refreshExpiration / 1000));
    response.addCookie(refreshCookie);

    // ✅ AccessToken만 JSON 형태로 응답 바디에 넣기
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

//     String json = String.format("{\"accessToken\": \"%s\"}", accessToken);
//     response.getWriter().write(json);
// }

String json = "{\"accessToken\": \"" + accessToken + "\"}";
response.getWriter().write(json);

}}
