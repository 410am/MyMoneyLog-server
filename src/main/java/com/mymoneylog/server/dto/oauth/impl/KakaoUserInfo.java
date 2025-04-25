package com.mymoneylog.server.dto.oauth.impl;

import com.mymoneylog.server.dto.oauth.OAuthUserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo implements OAuthUserInfoDTO {
    private String email;
    private String nickname;
}
