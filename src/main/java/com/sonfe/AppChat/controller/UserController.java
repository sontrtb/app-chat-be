package com.sonfe.AppChat.controller;

import com.sonfe.AppChat.dto.request.UserUpdateRequest;
import com.sonfe.AppChat.dto.response.ApiResponse;
import com.sonfe.AppChat.dto.response.UserResponse;
import com.sonfe.AppChat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping()
    ApiResponse<List<UserResponse>> getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication.getAuthorities().toString());

        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<List<UserResponse>>();
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        apiResponse.setResult(userService.updateUser(userId ,request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUser(@PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        return apiResponse;
    }
}
