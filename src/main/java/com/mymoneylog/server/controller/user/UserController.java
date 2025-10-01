package com.mymoneylog.server.controller.user;

import com.mymoneylog.server.dto.user.req.UserReqDTO;
import com.mymoneylog.server.dto.user.res.UserResDTO;
import com.mymoneylog.server.service.user.UserService;
import com.mymoneylog.server.utils.ApiResponseEntity;
import com.mymoneylog.server.utils.CommonConstants;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ 유저 생성
    @PostMapping("")
    public ApiResponseEntity<UserResDTO> createUser(@RequestBody @Valid UserReqDTO userReqDto) {
        UserResDTO savedUser = userService.createUser(userReqDto);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, savedUser);
    }

    // ✅ 유저 조회
    @GetMapping("/me")
    public ApiResponseEntity<UserResDTO> getUser(@AuthenticationPrincipal Long userId) {
        UserResDTO user = UserResDTO.from(userService.findUserById(userId));
        System.out.println("👉 Principal userId = " + userId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, user);
    }

    // ✅ 유저 수정
    @PostMapping("/me")
    public ApiResponseEntity<UserResDTO> updateUser(@AuthenticationPrincipal Long userId, @RequestBody UserReqDTO userReqDto) {
        UserResDTO updatedUser = userService.updateUser(userId, userReqDto);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, updatedUser);
    }

    // ✅ 유저 삭제 
    @DeleteMapping("/me")
    public ApiResponseEntity<UserResDTO> deleteUser(@AuthenticationPrincipal Long userId) {
        userService.deleteUser(userId);
        return ApiResponseEntity.ok(CommonConstants.GLOBAL_SUCCESS_MSG, null);
    }
}
