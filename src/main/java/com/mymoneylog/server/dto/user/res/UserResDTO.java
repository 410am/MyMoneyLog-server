package com.mymoneylog.server.dto.user.res;

import com.mymoneylog.server.entity.user.User;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResDTO {

    private Long userId;
    private String email;
    private String nickname;


    public static UserResDTO from(User user) {
        return UserResDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
