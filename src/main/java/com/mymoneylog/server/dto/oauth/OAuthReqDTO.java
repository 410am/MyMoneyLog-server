package com.mymoneylog.server.dto.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthReqDTO {
    private String provider; // "kakao" or "google"
    private String code;     // OAuth 인증 후 받은 인가 코드
}
