package cau.capstone2.tatoo.scar.controller;

import cau.capstone2.tatoo.auth.component.JwtTokenProvider;
import cau.capstone2.tatoo.scar.dto.RequestScarDto;
import cau.capstone2.tatoo.scar.service.ScarService;
import cau.capstone2.tatoo.user.service.UserService;
import cau.capstone2.tatoo.util.api.ApiResponse;
import cau.capstone2.tatoo.util.api.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scar")
public class ScarController {

    private final UserService userService;
    private final ScarService scarService;
    private final JwtTokenProvider jwtTokenProvider;

    //유저의 타투 학습 요청
    @Operation(summary = "유저의 타투 학습 요청")
    @PostMapping("/requestTattoo")
    public ApiResponse<Void> requestTatoo(@RequestHeader String accessToken, @RequestBody RequestScarDto requestScarDto, @RequestParam MultipartFile scarImage) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        scarService.requestTattoo(requestScarDto, userId, scarImage);
        return ApiResponse.success(null, ResponseCode.USER_TATTOO_REQUEST_SUCCESS.getMessage());
    }
}
