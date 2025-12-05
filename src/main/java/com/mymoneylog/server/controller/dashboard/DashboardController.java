package com.mymoneylog.server.controller.dashboard;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mymoneylog.server.dto.dashboard.DashboardResDTO;
import com.mymoneylog.server.service.dashboard.DashboardService;
import com.mymoneylog.server.utils.ApiResponseEntity;
import com.mymoneylog.server.utils.CommonConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


       
       @GetMapping("")
       public ApiResponseEntity<DashboardResDTO> getDashboard(@AuthenticationPrincipal Long userId,
        @RequestParam int year,
        @RequestParam int month) {
        DashboardResDTO dashboard = dashboardService.getDashboard(userId, year, month);
           return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, dashboard);
       }
}
