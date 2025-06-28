package com.mymoneylog.security;

import org.springframework.stereotype.Component;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // 구글에서 로그인한 유저의 정보를 받아오는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 유저 정보 불러오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 필요한 권한 부여 + 반환 (ROLE_USER 권한)
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuth2User.getAttributes(),
            "sub" // 사용자의 고유 식별자 키 (구글은 "sub")
        );
    }
}