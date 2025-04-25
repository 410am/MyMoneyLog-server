package com.mymoneylog.server.service.oauth.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymoneylog.server.dto.oauth.OAuthUserInfoDTO;
import com.mymoneylog.server.dto.oauth.impl.KakaoUserInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final ObjectMapper objectMapper;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String KAKAO_CLIENT_ID = "카카오 REST API 키"; // TODO: yml에서 꺼내도록
    private final String KAKAO_REDIRECT_URI = "프론트 리다이렉트 URI"; // TODO: 설정 맞게

    public OAuthUserInfoDTO getUserInfo(String code) throws Exception {
        // 1. 인가코드로 토큰 요청
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", KAKAO_CLIENT_ID);
        params.put("redirect_uri", KAKAO_REDIRECT_URI);
        params.put("code", code);

        HttpEntity<Map<String, String>> tokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(KAKAO_TOKEN_URL, tokenRequest, String.class);

        String accessToken = objectMapper.readTree(tokenResponse.getBody()).get("access_token").asText();

        // 2. 토큰으로 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                userRequest,
                String.class
        );

        JsonNode userJson = objectMapper.readTree(userResponse.getBody());
        String email = userJson.get("kakao_account").get("email").asText();
        String nickname = userJson.get("properties").get("nickname").asText();

        return new KakaoUserInfo(email, nickname);
    }
}
