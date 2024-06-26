package com.sonfe.AppChat.exception;

import com.sonfe.AppChat.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.sonfe.AppChat.exception.ErrorCode.UN_KNOW_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {

        log.error(exception.toString());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setErrorCode(UN_KNOW_ERROR.getCode());
        apiResponse.setMessage(UN_KNOW_ERROR.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setErrorCode(errorCode.getCode());
        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey =  exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY_ERROR;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException err) {

        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setErrorCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return  ResponseEntity.badRequest().body(apiResponse);
    }
}
