package cau.capstone2.tatoo.user.controller;

import cau.capstone2.tatoo.auth.component.JwtTokenProvider;
import cau.capstone2.tatoo.user.service.UserService;
import cau.capstone2.tatoo.util.api.ApiResponse;
import cau.capstone2.tatoo.util.api.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //유저의 타투 촬영 여부(메인 화면 마이타투 유무 확인)
    @Operation(summary = "유저의 최초 학습 여부 확인")
    @GetMapping("/checkIsFirstUser")
    public ApiResponse<Boolean> checkTrained(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.checkTrained(userId), ResponseCode.USER_STATE_SUCCESS.getMessage());
    }
}
