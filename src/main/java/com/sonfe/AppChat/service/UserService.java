package com.sonfe.AppChat.service;

import com.sonfe.AppChat.dto.request.UserCreationRequest;
import com.sonfe.AppChat.dto.request.UserUpdateRequest;
import com.sonfe.AppChat.dto.response.UserResponse;
import com.sonfe.AppChat.entity.User;
import com.sonfe.AppChat.enums.Role;
import com.sonfe.AppChat.exception.AppException;
import com.sonfe.AppChat.exception.ErrorCode;
import com.sonfe.AppChat.mapper.UserMapper;
import com.sonfe.AppChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER.name());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        List<User> users =  userRepository.findAll();
        List<UserResponse> listUserResponse = new ArrayList<UserResponse>(users.size());
        for (User user : users) {
            listUserResponse.add(userMapper.toUserResponse(user));
        }
        return  listUserResponse;
    }

    public UserResponse getUser(Long id) {
        return  userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))) ;
    }

    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
