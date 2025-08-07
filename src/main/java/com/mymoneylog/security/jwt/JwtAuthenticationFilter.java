package com.mymoneylog.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
        // 🔹 1. Authorization 헤더에서 Bearer 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 🔹 2. 토큰 유효성 검증
            if (jwtProvider.validateToken(token)) {
                String userId = jwtProvider.getSubject(token);
                String role = jwtProvider.getRole(token);

                // 🔹 3. 인증 정보 생성 (사용자 정보 조회 생략하고, 이메일만 사용)
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, role, List.of());

                // 🔹 4. 시큐리티 컨텍스트에 인증 정보 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 🔹 5. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}

