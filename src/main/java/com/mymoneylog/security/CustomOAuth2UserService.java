package com.mymoneylog.security;

import org.springframework.stereotype.Component;

import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글에서 로그인한 유저의 정보를 받아오는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 유저 정보 불러오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String sub = oAuth2User.getAttribute("sub"); // Google ID
        String email = oAuth2User.getAttribute("email"); // 이메일
        String name = oAuth2User.getAttribute("name"); // 이름

        // 이미 등록된 유저인지 확인
        User user = userRepository.findByProviderId(sub).orElseGet(() -> {
            // 없으면 새로 저장
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setNickname(name);
            newUser.setProviderId(sub);
            return userRepository.save(newUser);
        });

        // 필요한 권한 부여 + 반환 (ROLE_USER 권한)
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuth2User.getAttributes(),
            "sub" // 사용자의 고유 식별자 키 (구글은 "sub")
        );
    }
}