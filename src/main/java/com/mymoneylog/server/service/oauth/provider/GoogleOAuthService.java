package com.mymoneylog.server.service.oauth.provider;

import com.mymoneylog.server.dto.oauth.OAuthReqDTO;
import com.mymoneylog.server.dto.oauth.OAuthResDTO;
import com.mymoneylog.server.dto.oauth.OAuthUserInfoDTO;
import com.mymoneylog.server.dto.oauth.impl.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String clientId = "YOUR_GOOGLE_CLIENT_ID";
    private final String clientSecret = "YOUR_GOOGLE_CLIENT_SECRET";
    private final String redirectUri = "YOUR_GOOGLE_REDIRECT_URI";

    public OAuthResDTO handleGoogleOAuthLogin(OAuthReqDTO request) {
        OAuthUserInfoDTO userInfo = getUserInfo(request.getCode());

        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        return OAuthResDTO.builder()
                .email(email)
                .nickname(nickname)
                .provider("google")
                .build();
    }

    public OAuthUserInfoDTO getUserInfo(String code) {
        Map<String, Object> userInfo = getUserInfoFromGoogle(code);
        return new GoogleUserInfo(
            (String) userInfo.get("email"),
            (String) userInfo.get("name")
        );
    }

    private Map<String, Object> getUserInfoFromGoogle(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.GET,
            userInfoRequest,
            Map.class
        );

        return userInfoResponse.getBody();
    }
}
