package com.mymoneylog.server.controller.oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.server.dto.oauth.OAuthReqDTO;
import com.mymoneylog.server.dto.oauth.OAuthResDTO;
import com.mymoneylog.server.service.oauth.OAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor    
public class OAuthController {

    private final OAuthService OAuthService;

    @PostMapping("/oauth")
    public ResponseEntity<?> loginOrSignup(@RequestBody OAuthReqDTO request) {
        OAuthResDTO response = OAuthService.handleOAuthLogin(request);
        return ResponseEntity.ok(response);
    }
}
