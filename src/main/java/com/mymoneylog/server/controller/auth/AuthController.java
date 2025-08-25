package com.mymoneylog.server.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mymoneylog.server.service.auth.GoogleOAuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String idToken = request.get("idToken");
        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body("idToken이 없습니다");
        }
        String email = request.get("email");

        Map<String, String> tokens = googleOAuthService.handleGoogleOAuthLogin(idToken, response);

        Map<String, Object> body = new HashMap<>();
        body.put("data", tokens);
    
        return ResponseEntity.ok(body);
    

        // return ResponseEntity.ok(Map.of("data", tokens, "email", email));  // 프론트는 res.data.data.jwt 로 받을 수 있음
    }
}
