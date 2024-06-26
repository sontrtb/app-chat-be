package com.sonfe.AppChat.mapper;

import com.sonfe.AppChat.dto.request.UserCreationRequest;
import com.sonfe.AppChat.dto.request.UserUpdateRequest;
import com.sonfe.AppChat.dto.response.UserResponse;
import com.sonfe.AppChat.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserCreationRequest request);
    UserResponse toUserResponse (User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
