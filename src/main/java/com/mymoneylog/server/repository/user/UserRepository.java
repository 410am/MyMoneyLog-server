package com.mymoneylog.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mymoneylog.server.entity.user.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);

    // ID로 유저 찾기
    Optional<User> findByUserId(Long userId);

    // 구글 sub로 유저 찾기
    Optional<User> findByProviderId(String sub);
    
}
