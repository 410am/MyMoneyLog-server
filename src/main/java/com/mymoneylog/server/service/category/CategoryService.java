package com.mymoneylog.server.service.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mymoneylog.server.dto.category.CategoryReqDTO;
import com.mymoneylog.server.dto.category.CategoryResDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.entity.record.Record;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.category.CategoryRepository;
import com.mymoneylog.server.repository.record.RecordRepository;
import com.mymoneylog.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor    
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    // 카테고리 생성
    public CategoryResDTO createCategory(CategoryReqDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Category category = Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .isDefault(dto.isDefault())
                .user(user)
                .build();

        return CategoryResDTO.from(categoryRepository.save(category));
    }


    // 카테고리 id로 카테고리 조회
    @Transactional(readOnly = true)
    public CategoryResDTO getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        return CategoryResDTO.from(category);
    }


    // userId로 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategoryResDTO> getCategoriesByUserId(Long userId) {
        List<Category> categories = categoryRepository.findByUserUserId(userId);
        return categories.stream()
        .map(CategoryResDTO::from)
        .collect(Collectors.toList());
    }
    
    // 카테고리 수정
    public CategoryResDTO updateCategory(Long categoryId, CategoryReqDTO dto) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        
        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setIsDefault(dto.isDefault());

        return CategoryResDTO.from(categoryRepository.save(category));
    }


    // 카테고리 삭제
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }


    // 카테고리 id로 카테고리+기록 조회
    public List<RecordResDTO> getCategoryAndRecordsById(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        
        List<RecordResDTO> records = recordRepository.findByCategoryCategoryId(categoryId)
                .stream()
                .map(RecordResDTO::from)
                .collect(Collectors.toList());

        return records;
    }

}
