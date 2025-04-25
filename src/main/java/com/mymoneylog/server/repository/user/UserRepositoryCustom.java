package com.mymoneylog.server.repository.user;
import com.mymoneylog.server.entity.user.User;

public interface UserRepositoryCustom {
    User findUserById(Long id);
    void deleteUserById(Long id);
}