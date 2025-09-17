// package com.mymoneylog.security.jwt;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;
// import java.util.List;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtProvider jwtProvider;

//     public JwtAuthenticationFilter(JwtProvider jwtProvider) {
//         this.jwtProvider = jwtProvider;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain) throws ServletException, IOException {
//         // 🔹 1. Authorization 헤더에서 Bearer 토큰 추출
//         String authHeader = request.getHeader("Authorization");
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             String token = authHeader.substring(7);
//             System.out.println("✅ token = " + token);

//             // 🔹 2. 토큰 유효성 검증
//             if (jwtProvider.validateToken(token)) {
//                 String userId = jwtProvider.getSubject(token);
//                 String role = jwtProvider.getRole(token);

//                 System.out.printf("✅ AccessToken 유효. userId={}, role={}", userId, role);
            

//                 // 🔹 3. 인증 정보 생성 (사용자 정보 조회 생략하고, 이메일만 사용)
//                 UsernamePasswordAuthenticationToken authentication =
//                     // new UsernamePasswordAuthenticationToken(userId, role, List.of());
//                     new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(role)));

//                 // 🔹 4. 시큐리티 컨텍스트에 인증 정보 등록
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             } else {
//                         // ❌ 유효하지 않거나 만료된 토큰 → 401 반환
//                         System.out.println("❌ AccessToken 유효하지 않음 (만료 or 잘못된 토큰)");
//                         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                         return;

//             }
//         }

//         // 🔹 5. 다음 필터로 진행
//         filterChain.doFilter(request, response);
//     }
// }

package com.mymoneylog.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

                                        String path = request.getRequestURI();

    // refresh 요청은 accessToken 검사 스킵
    if ("/refresh".equals(path) || "/auth/refresh".equals(path) || path.startsWith("/auth/")) {
        filterChain.doFilter(request, response);
        return;
    }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                var claims = jwtProvider.parseClaims(token); // validate 대신 Claims 리턴
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);
        
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null,
                        List.of(new SimpleGrantedAuthority(role)));
        
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                System.out.println("❌ AccessToken 만료됨: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
