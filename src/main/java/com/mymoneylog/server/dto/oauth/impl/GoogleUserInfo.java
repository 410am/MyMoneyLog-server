package com.mymoneylog.server.dto.oauth.impl;

import com.mymoneylog.server.dto.oauth.OAuthUserInfoDTO;
import lombok.Getter;

@Getter
public class GoogleUserInfo implements OAuthUserInfoDTO {
    private final String email;
    private final String nickname;

    public GoogleUserInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
} 