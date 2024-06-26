package com.sonfe.AppChat.controller;

import com.nimbusds.jose.JOSEException;
import com.sonfe.AppChat.dto.request.AuthenticationRequest;
import com.sonfe.AppChat.dto.request.IntrospectRequest;
import com.sonfe.AppChat.dto.request.UserCreationRequest;
import com.sonfe.AppChat.dto.response.ApiResponse;
import com.sonfe.AppChat.dto.response.AuthenticationResponse;
import com.sonfe.AppChat.dto.response.IntrospectResponse;
import com.sonfe.AppChat.dto.response.UserResponse;
import com.sonfe.AppChat.service.AuthenticationService;
import com.sonfe.AppChat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest  request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        return  ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        return  ApiResponse.<IntrospectResponse>builder().result(result).build();
    }
}
