package com.mymoneylog.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.mymoneylog.server.entity.category.DefaultCategory;
import com.mymoneylog.server.enums.IncomeExpenseType;
import com.mymoneylog.server.repository.category.DefaultCategoryRepository;

@Component
@RequiredArgsConstructor
public class DefaultCategoryInitializer implements CommandLineRunner {

    private final DefaultCategoryRepository defaultCategoryRepository;

    @Override
    public void run(String... args) {
        if (defaultCategoryRepository.count() == 0) {
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("식비")
                    .type(IncomeExpenseType.EXPENSE)
                    .build());
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("교통비")
                    .type(IncomeExpenseType.EXPENSE)
                    .build());
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("월세")
                    .type(IncomeExpenseType.EXPENSE)
                    .build());
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("카페/디저트")
                    .type(IncomeExpenseType.EXPENSE)
                    .build());
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("월급")
                    .type(IncomeExpenseType.INCOME)
                    .build());
            defaultCategoryRepository.save(DefaultCategory.builder()
                    .name("보너스")
                    .type(IncomeExpenseType.INCOME)
                    .build());
            
            System.out.println("✅ 기본 카테고리 자동 초기화 완료!");
        } else {
            System.out.println("ℹ️ 기본 카테고리가 이미 존재함, 초기화 스킵");
        }
    }
}
