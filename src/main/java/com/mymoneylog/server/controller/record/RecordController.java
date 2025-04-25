package com.mymoneylog.server.controller.record;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.server.dto.record.RecordReqDTO;
import com.mymoneylog.server.dto.record.RecordResDTO;
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
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, response);
    }

    // 기록 전체 조회 (유저 기준)
    @GetMapping("/user/{userId}")
    public ApiResponseEntity<List<RecordResDTO>> getRecordsByUser(@PathVariable Long userId) {
        List<RecordResDTO> records = recordService.getRecordsByUser(userId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, records);
    }

    // 단일 기록 조회
    @GetMapping("/{recordId}")
    public ApiResponseEntity<RecordResDTO> getRecordById(@PathVariable Long recordId) {
        RecordResDTO record = recordService.getRecordById(recordId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, record);
    }


    // // 내용별 기록 조회(카테고리, 내용)
    // @GetMapping("/search")
    // public ResponseEntity<List<RecordResDTO>> searchRecords(@PathVariable Long userId, @RequestParam String keyword) {
    //     List<RecordResDTO> records = recordService.recordAndCategorySearch(userId, keyword);
    //     return ResponseEntity.ok(records);
    // }

    // 기록 수정
    @PostMapping("/{recordId}")
    public ApiResponseEntity<RecordResDTO> updateRecord(
            @PathVariable Long recordId,
            @RequestBody RecordReqDTO reqDTO
    ) {
        RecordResDTO updated = recordService.updateRecord(recordId, reqDTO);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, updated);
    }

    // 기록 삭제
    @DeleteMapping("/{recordId}")
    public ApiResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        recordService.deleteRecord(recordId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, null);
    }
}
