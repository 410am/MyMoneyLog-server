package com.mymoneylog.server.service.oauth;

import com.mymoneylog.server.dto.oauth.OAuthReqDTO;
import com.mymoneylog.server.dto.oauth.OAuthResDTO;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;
import com.mymoneylog.server.service.oauth.provider.GoogleOAuthService;
import com.mymoneylog.server.service.oauth.provider.KakaoOAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;

    @Transactional
    public OAuthResDTO handleOAuthLogin(OAuthReqDTO request) {
        String provider = request.getProvider().toLowerCase();
        String code = request.getCode();

        String email;
        String nickname;

        switch (provider) {
            case "kakao" -> {
                try {
                    var kakaoUser = kakaoOAuthService.getUserInfo(code);
                    email = kakaoUser.getEmail();
                    nickname = kakaoUser.getNickname();
                } catch (Exception e) {
                    throw new RuntimeException("카카오 로그인 처리 중 오류 발생", e);
                }
            }
            case "google" -> {
                var googleUser = googleOAuthService.getUserInfo(code);
                email = googleUser.getEmail();
                nickname = googleUser.getNickname();
            }
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .password("oauth") // dummy
                        .build()));

        updateUser(user, nickname);

        return OAuthResDTO.from(user);
    }

    private void updateUser(User user, String nickname) {
        try {
            user.setNickname(nickname);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 업데이트 중 오류 발생", e);
        }
    }
}
