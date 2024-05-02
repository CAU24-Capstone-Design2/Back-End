package cau.capstone2.tatoo.scar.controller;

import cau.capstone2.tatoo.auth.component.JwtTokenProvider;
import cau.capstone2.tatoo.s3.dto.RequestTattooDto;
import cau.capstone2.tatoo.s3.dto.ResponseTattooDto;
import cau.capstone2.tatoo.scar.service.ScarService;
import cau.capstone2.tatoo.util.api.ApiResponse;
import cau.capstone2.tatoo.util.api.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scar")
public class ScarController {

    private final ScarService scarService;
    private final JwtTokenProvider jwtTokenProvider;

    //유저의 타투 학습 요청
    @Operation(summary = "유저의 타투 학습 요청")
    @PostMapping("/requestTattoo")
    public ApiResponse<Void> requestTatoo(@RequestHeader String accessToken, @ModelAttribute RequestTattooDto requestTattooDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        scarService.requestTattoo(requestTattooDto, userId);
        return ApiResponse.success(null, ResponseCode.USER_TATTOO_REQUEST_SUCCESS.getMessage());
    }

    //유저 타투 정보 반환
    @Operation(summary = "유저 타투 모든 정보 반환")
    @GetMapping("/{scarId}/getTattooAllInfo")
    public ApiResponse<ResponseTattooDto> getTattoo(@PathVariable Long scarId) {
        return ApiResponse.success(scarService.responseTattoo(scarId), ResponseCode.USER_TATTOO_GET_SUCCESS.getMessage());
    }

    //유저의 모든 타투 도안 반환
    @Operation(summary = "유저의 모든 타투 도안 반환")
    @GetMapping("/getAllTattoo")
    public ApiResponse<List<ResponseTattooDto>> getAllTattoo(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(scarService.getUserTattoo(userId), ResponseCode.USER_TATTOO_GET_SUCCESS.getMessage());
    }

    //테스트를 위한 aws s3 스토리지에 이미지 업로드 테스트 (multipartfile -> s3)
    @Operation(summary = "테스트를 위한 aws s3 스토리지에 이미지 업로드 테스트")
    @PostMapping("/uploadImage")
    public ApiResponse<String> uploadImage(@ModelAttribute RequestTattooDto dto) throws IOException {
        log.info(dto.getStyleDescription());
        String res = scarService.uploadImage(dto.getScarImage());
        log.info(res);
        return ApiResponse.success(res, ResponseCode.USER_TATTOO_GET_SUCCESS.getMessage());
    }

    //테스트를 위한 aws s3 스토리지에 이미지 업로드 테스트 (file -> s3)
    @Operation(summary = "테스트를 위한 aws s3 스토리지에 이미지 업로드 테스트")
    @PostMapping("/uploadImagePath")
    public ApiResponse<String> uploadFileImage() throws IOException {
        log.info("경로:");
        String res = scarService.uploadImageFromFile("/Users/cryptolab/Desktop/tattoo1.jpeg");
        return ApiResponse.success(res, ResponseCode.USER_TATTOO_GET_SUCCESS.getMessage());
    }

}
