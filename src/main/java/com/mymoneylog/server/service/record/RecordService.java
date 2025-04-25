package com.mymoneylog.server.service.record;

import com.mymoneylog.server.dto.record.RecordReqDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.entity.record.QRecord;
import com.mymoneylog.server.entity.record.Record;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.category.CategoryRepository;
import com.mymoneylog.server.repository.record.RecordRepository;
import com.mymoneylog.server.repository.user.UserRepository;
import com.mymoneylog.server.utils.CustomException;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final JPAQueryFactory queryFactory;

    
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

    // 유저별 기록 조회
    @Transactional(readOnly = true)
    public List<RecordResDTO> getRecordsByUser(Long userId) {
        List<Record> records = recordRepository.findByUserUserId(userId);

        int a = 1;

        if (a == 1) {
            log.warn("존재하지 않는 회원!!");
            throw new CustomException("존재하지 않는 회원입니다.");
        }

        return records.stream()
                .map(RecordResDTO::from)
                .collect(Collectors.toList());
    }

    // 단일 기록 조회(recordId)
    @Transactional(readOnly = true)
    public RecordResDTO getRecordById(Long recordId) {
        Record record = recordRepository.findWithUserAndCategoryById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));

        return RecordResDTO.from(record);
    }


    // 내용별 기록 조회(카테고리, 내용)
//     public List<Record> recordAndCategorySearch(Long userId, String keyword) {

//         QRecord record = QRecord.record;
    
//         return queryFactory
//             .selectFrom(record)
//             .where(
//                 record.user.userId.eq(userId)
//                     .and(
//                         record.memo.containsIgnoreCase(keyword)
//                         .or(record.category.name.containsIgnoreCase(keyword))
//                     )
//             )
//             .fetch();
//     }
    


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
