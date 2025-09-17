// package com.mymoneylog.security;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseCookie;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import com.mymoneylog.security.jwt.JwtProvider;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.security.oauth2.core.user.OAuth2User;

// import java.io.IOException;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    

//     private final JwtProvider jwtProvider;

//     @Value("${jwt.access-expiration}")
//     private long accessExpiration; 

//     @Value("${jwt.refresh-expiration}")
//     private long refreshExpiration; 

// @Override
// public void onAuthenticationSuccess(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     Authentication authentication) throws IOException {

//                                         log.info("✅ onAuthenticationSuccess 실행됨!!!");
                                        
//     // 🔹 로그인한 사용자 정보 가져오기
//     OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

//     // 🔹 사용자 식별자 및 권한
//     String userId = oAuth2User.getAttribute("sub");
//     String role = "ROLE_USER";

//     // 🔹 JWT 생성
//     String accessToken = jwtProvider.createToken(userId, role, accessExpiration);
//     String refreshToken = jwtProvider.createToken(userId, role, refreshExpiration);

//     // // 🔹 RefreshToken을 HttpOnly 쿠키로 설정
//     // Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//     // refreshCookie.setHttpOnly(true);
//     // refreshCookie.setSecure(true); // HTTPS 환경에서만 동작
//     // refreshCookie.setPath("/");
//     // refreshCookie.setMaxAge((int) (refreshExpiration / 1000));
//     // response.addCookie(refreshCookie);

    

// ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
// .httpOnly(true)
// .secure(true) // HTTPS 환경일 때만 true
// .sameSite("None") // ✅ SameSite 옵션 확실히 지정
// .path("/")
// .maxAge(refreshExpiration / 1000)
// .build();

// response.addHeader("Set-Cookie", cookie.toString());

// System.out.println("✅ onAuthenticationSuccess 실행됨");
// System.out.println(cookie.toString());
// log.info("✅ refreshToken Set-Cookie = {}", cookie.toString());

//     // // ✅ RefreshToken 쿠키를 Set-Cookie 헤더로 직접 추가
//     // String refreshCookie = String.format(
//     //         "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
//     //         refreshToken,
//     //         refreshExpiration / 1000
//     // );
//     // response.addHeader("Set-Cookie", refreshCookie);

//     // ✅ AccessToken만 JSON 형태로 응답 바디에 넣기
//     response.setContentType("application/json");
//     response.setCharacterEncoding("UTF-8");


// String json = "{\"accessToken\": \"" + accessToken + "\"}";
// response.getWriter().write(json);

// }}
