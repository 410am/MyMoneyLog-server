package com.mymoneylog.server.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mymoneylog.server.entity.category.DefaultCategory;

public interface DefaultCategoryRepository extends JpaRepository<DefaultCategory, Long> {
    DefaultCategory findByDefaultCategoryId(Long defaultCategoryId);
    DefaultCategory findByName(String name);
}
