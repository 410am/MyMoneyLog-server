package com.mymoneylog.server.controller.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.server.dto.category.CategoryReqDTO;
import com.mymoneylog.server.dto.category.CategoryResDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.service.category.CategoryService;
import com.mymoneylog.server.utils.ApiResponseEntity;
import com.mymoneylog.server.utils.CommonConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ApiResponseEntity<?> createCategory(@RequestBody @Valid CategoryReqDTO dto) {
        CategoryResDTO saved = categoryService.createCategory(dto);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, saved);
    }

    // 카테고리 id로 카테고리 조회
    @GetMapping("/{categoryId}")
    public ApiResponseEntity<?> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        CategoryResDTO category = categoryService.getCategoryById(categoryId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, category);
    }

    // userId로 카테고리 조회
    @GetMapping("/user/me")
    public ApiResponseEntity<?> getCategoriesByUserId(@AuthenticationPrincipal Long userId) {
        List<CategoryResDTO> categories = categoryService.getCategoriesByUserId(userId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, categories);
    }

    // 카테고리 id로 카테고리+기록 조회
    @GetMapping("/{categoryId}/records")
    public ApiResponseEntity<?> getCategoryAndRecordsById(@PathVariable("categoryId") Long categoryId) {
        List<RecordResDTO> categoryAndRecords = categoryService.getCategoryAndRecordsById(categoryId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, categoryAndRecords);
    }


    // 카테고리 수정
    @PostMapping("/{categoryId}")
    public ApiResponseEntity<?> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody @Valid CategoryReqDTO dto) {
        CategoryResDTO updatedCategory = categoryService.updateCategory(categoryId, dto);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, updatedCategory);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ApiResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, null);
    }
}

