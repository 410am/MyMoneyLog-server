package com.mymoneylog.server.service.user;

import com.mymoneylog.server.dto.user.req.UserReqDTO;
import com.mymoneylog.server.dto.user.res.UserResDTO;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    private String storeProfileImage(MultipartFile file) {
 
        try {
            // 파일 검증
            if (file.isEmpty()) {
                throw new IllegalArgumentException("빈 파일입니다.");
            }
    
            // 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String extension = "";
    
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
    
            // 저장할 파일명 생성 (중복 방지)
            String savedFileName = UUID.randomUUID() + extension;
    
            // 저장 경로
            Path uploadPath = Paths.get(
                System.getProperty("user.dir"),
                "uploads",
                "profile"
            );
            Files.createDirectories(uploadPath); // 폴더 없으면 생성
    
            Path filePath = uploadPath.resolve(savedFileName);

            System.out.println("파일 이름: " + file.getOriginalFilename());
            System.out.println("파일 사이즈: " + file.getSize());
            System.out.println("저장 경로: " + uploadPath.toAbsolutePath());
    
            // 파일 저장
            file.transferTo(filePath.toFile());
    
            // 프론트에서 접근할 URL 반환
            return "/images/profile/" + savedFileName;
    
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }
    

    // ✅ 유저 생성
    public UserResDTO createUser(UserReqDTO userReqDto) {
        User user = User.builder()
                .email(userReqDto.getEmail())
                // .password(userReqDto.getPassword())
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
    public UserResDTO updateUser(Long id, String nickname, MultipartFile pictureFile) {
        User user = findUserById(id);
        user.update(nickname);
        if (pictureFile != null && !pictureFile.isEmpty()) {
            String imageUrl = storeProfileImage(pictureFile);
            user.setPicture(imageUrl);
          }
  
        User updatedUser = userRepository.save(user);
       
        return UserResDTO.from(updatedUser);
    }



    // ✅ 유저 삭제
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));
        userRepository.delete(user); 
    }

    public Long findIdByProviderId(String sub) {
        return userRepository.findByProviderId(sub)
                .map(User::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for providerId: " + sub));
    }
}
