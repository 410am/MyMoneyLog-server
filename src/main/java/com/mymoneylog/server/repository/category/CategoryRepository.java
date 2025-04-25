package com.mymoneylog.server.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.entity.user.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserUserId(Long userId);


}
