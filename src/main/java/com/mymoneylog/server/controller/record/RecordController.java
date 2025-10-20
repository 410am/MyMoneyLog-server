// 
package com.mymoneylog.server.controller.record;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mymoneylog.server.dto.record.RecordReqDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
import com.mymoneylog.server.enums.IncomeExpenseType;
import com.mymoneylog.server.service.record.RecordService;
import com.mymoneylog.server.utils.ApiResponseEntity;
import com.mymoneylog.server.utils.CommonConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    // 기록 생성
    @PostMapping
    public ApiResponseEntity<RecordResDTO> createRecord(@RequestBody RecordReqDTO reqDTO) {
        RecordResDTO response = recordService.createRecord(reqDTO);
        System.out.println("🎯 받은 DTO: " + reqDTO);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, response);
    }

    // ✅ 프론트 호출과 동일: GET /record/user/{userId}?categoryId=&type=&from=&to=&sort=&page=&size=
    @GetMapping("/user/me")
    public ApiResponseEntity<Page<RecordResDTO>> getRecordsByUser(
            // @PathVariable("userId") Long userId,
            @AuthenticationPrincipal Long userId,
            @RequestParam(name = "categoryId",required = false) Long categoryId,
            @RequestParam(name = "type",required = false) IncomeExpenseType type, // INCOME/EXPENSE
            @RequestParam(name = "from",required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to",required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "sort",defaultValue = "date_desc") String sort,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "20") int size,
            @RequestParam(name = "search", defaultValue = "") String search
    ) {

        // sort 문자열 → Sort 매핑
        Sort s = switch (sort) {
            case "date_asc"    -> Sort.by(Sort.Order.asc("date"), Sort.Order.asc("recordId"));
            case "amount_desc" -> Sort.by(Sort.Order.desc("amount"), Sort.Order.desc("recordId"));
            case "amount_asc"  -> Sort.by(Sort.Order.asc("amount"), Sort.Order.asc("recordId"));
            default            -> Sort.by(Sort.Order.desc("date"), Sort.Order.desc("recordId"));
        };
        Pageable pageable = PageRequest.of(page, size, s);

        // to를 inclusive로 쓰려면 '< to+1일' 비교를 위해 보정
        LocalDate toExclusive = (to != null) ? to.plusDays(1) : null;

        Page<RecordResDTO> result = recordService.findPageByUser(
            userId, categoryId,search, type, from, toExclusive, pageable
        );
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, result);
    }

    // 단일 기록 조회
    @GetMapping("/{recordId}")
    public ApiResponseEntity<RecordResDTO> getRecordById(@PathVariable("recordId") Long recordId) {
        RecordResDTO record = recordService.getRecordById(recordId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, record);
    }

    // 기록 수정
    @PostMapping("/{recordId}")
    public ApiResponseEntity<RecordResDTO> updateRecord(
            @PathVariable("recordId") Long recordId,
            @RequestBody RecordReqDTO reqDTO
    ) {
        RecordResDTO updated = recordService.updateRecord(recordId, reqDTO);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, updated);
    }

    // 기록 삭제
    @DeleteMapping("/{recordId}")
    public ApiResponseEntity<Void> deleteRecord(@PathVariable("recordId") Long recordId) {
        recordService.deleteRecord(recordId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, null);
    }
}
