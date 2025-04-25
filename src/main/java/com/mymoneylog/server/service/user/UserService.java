package com.mymoneylog.server.service.user;

import com.mymoneylog.server.dto.user.req.UserReqDTO;
import com.mymoneylog.server.dto.user.res.UserResDTO;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // ✅ 유저 생성
    public UserResDTO createUser(UserReqDTO userReqDto) {
        User user = User.builder()
                .email(userReqDto.getEmail())
                .password(userReqDto.getPassword())
                .nickname(userReqDto.getNickname())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user); // JPA 기본 save()

        return UserResDTO.from(savedUser);
    }

    // ✅ 유저 조회
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + id));
    }

    // ✅ 유저 수정
    public UserResDTO updateUser(Long id, UserReqDTO userReqDto) {
        User user = findUserById(id);
        user.update(userReqDto);

        User updatedUser = userRepository.save(user);
        return UserResDTO.from(updatedUser);
    }



    // ✅ 유저 삭제
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
