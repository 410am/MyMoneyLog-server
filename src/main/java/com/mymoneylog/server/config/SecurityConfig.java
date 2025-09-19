package com.mymoneylog.server.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mymoneylog.security.CustomOAuth2UserService;
// import com.mymoneylog.security.OAuth2SuccessHandler;
import com.mymoneylog.security.jwt.JwtProvider;
import com.mymoneylog.server.repository.user.UserRepository;
import com.mymoneylog.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(Collections.singletonList(List.of("http://localhost:5173", "http://localhost:4173")));
    configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 프리플라이트 허용

                    .requestMatchers("/user", "/user/**", "/ai-report", "/ai-report/**", "/category", "/category/**", "/record", "/record/**", "/login","/auth/**","/auth" ,"/login/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2.disable());
            // .oauth2Login(oauth2 -> oauth2
            // .userInfoEndpoint(userInfo -> userInfo
            //     .userService(customOAuth2UserService()) // 유저 정보 가져오는 서비스 지정
            // )
            // .successHandler(oAuth2SuccessHandler(jwtProvider()))
            // .defaultSuccessUrl("http://localhost:5173/login/success", true) // 리디렉트 위치 // 로그인 성공 시 실행할 핸들러 지정
            // );
            
    http.addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}

    // 유저 정보 처리를 담당할 커스텀 서비스 등록
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }

    // // 로그인 성공 시 처리할 커스텀 핸들러 등록
    // @Bean
    // public OAuth2SuccessHandler oAuth2SuccessHandler(JwtProvider jwtProvider) {
    //     return new OAuth2SuccessHandler(jwtProvider);
    // }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtSecret);
    }

    @Bean
    public JwtAuthenticationFilter JwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider());
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }



}
