package cau.capstone2.tatoo.util.exception;

import cau.capstone2.tatoo.util.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ApiResponse<Void> handleUserException(UserException e) {
        log.info("UserException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode(), null);
    }
}
