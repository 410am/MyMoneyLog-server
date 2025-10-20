

package com.mymoneylog.server.service.record;

import com.mymoneylog.server.dto.record.RecordReqDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.entity.record.Record;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.enums.IncomeExpenseType;
import com.mymoneylog.server.repository.category.CategoryRepository;
import com.mymoneylog.server.repository.record.RecordRepository;
import com.mymoneylog.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // 기록 생성
    public RecordResDTO createRecord(RecordReqDTO reqDTO) {



        User user = userRepository.findById(reqDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(reqDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Record record = Record.builder()
                .user(user)
                .category(category)
                .type(reqDTO.getType())
                .amount(reqDTO.getAmount())
                .memo(reqDTO.getMemo())
                .date(reqDTO.getDate())
                .build();

        return RecordResDTO.from(recordRepository.save(record));
    }

    // 유저별 기록 조회 (legacy)
    @Transactional(readOnly = true)
    public List<RecordResDTO> getRecordsByUser(Long userId) {
        List<Record> records = recordRepository.findByUserUserId(userId);

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다: " + userId);
        }

        return records.stream()
                .map(RecordResDTO::from)
                .collect(Collectors.toList());
    }

    // 단일 기록 조회
    @Transactional(readOnly = true)
    public RecordResDTO getRecordById(Long recordId) {
        Record record = recordRepository.findWithUserAndCategoryById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));
        return RecordResDTO.from(record);
    }

    // ✅ 페이지 + 필터
    @Transactional(readOnly = true)
    public Page<RecordResDTO> findPageByUser(
            Long userId,
            Long categoryId,            // optional
            String search,
            IncomeExpenseType type,     // optional (INCOME/EXPENSE)
            LocalDate from,             // optional (inclusive)
            LocalDate toExclusive,      // optional (exclusive; to+1day)
            Pageable pageable
    ) {
        // ⚠️ 엔티티 필드명이 user.userId / category.categoryId 라는 전제 (네 레포 시그니처 기준)
        Specification<Record> spec = (root, q, cb) ->
                cb.equal(root.get("user").get("userId"), userId);

        if (categoryId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("category").get("categoryId"), categoryId));
        }
        if (type != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("type"), type));
        }
        if (from != null) {
            spec = spec.and((root, q, cb) ->
                    cb.greaterThanOrEqualTo(root.get("date"), from));
        }
        if (toExclusive != null) {
            spec = spec.and((root, q, cb) ->
                    cb.lessThan(root.get("date"), toExclusive));
        }
        if (search != null && !search.isBlank()) {
                spec = spec.and((root, q, cb) ->
                        cb.like(root.get("memo"), "%" + search + "%"));
            }

        return recordRepository.findAll(spec, pageable)
                .map(RecordResDTO::from);
    }

    // 기록 수정
    public RecordResDTO updateRecord(Long recordId, RecordReqDTO reqDTO) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));

        Category category = categoryRepository.findById(reqDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        record.setCategory(category);
        record.setType(reqDTO.getType());
        record.setAmount(reqDTO.getAmount());
        record.setMemo(reqDTO.getMemo());
        record.setDate(reqDTO.getDate());

        return RecordResDTO.from(record);
    }

    // 기록 삭제
    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }
}
