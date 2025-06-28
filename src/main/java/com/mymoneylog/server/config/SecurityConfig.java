package com.mymoneylog.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.mymoneylog.security.CustomOAuth2UserService;
import com.mymoneylog.security.OAuth2SuccessHandler;
import com.mymoneylog.security.JwtProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/user", "/user/**", "/ai-report", "/ai-report/**", "/category", "/category/**", "/record", "/record/**", "/login","/auth/**","/auth" ,"/login/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService()) // 유저 정보 가져오는 서비스 지정
            )
            .successHandler(oAuth2SuccessHandler(jwtProvider())) // 로그인 성공 시 실행할 핸들러 지정
        );
    return http.build();
}

    // 유저 정보 처리를 담당할 커스텀 서비스 등록
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    // 로그인 성공 시 처리할 커스텀 핸들러 등록
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(JwtProvider jwtProvider) {
        return new OAuth2SuccessHandler(jwtProvider);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

}
