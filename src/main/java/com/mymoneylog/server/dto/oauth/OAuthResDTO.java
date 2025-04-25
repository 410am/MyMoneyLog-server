package com.mymoneylog.server.dto.oauth;

import com.mymoneylog.server.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthResDTO {
    private String accessToken; // JWT 토큰
    private boolean isNewUser;  // 회원가입 여부
    private Long userId;
    private String email;
    private String nickname;
    private String provider;    // OAuth 제공자 (google, kakao 등)

    public static OAuthResDTO from(User user) {
        return OAuthResDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .build();
    }
}