package com.sonfe.AppChat.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotEmpty(message = "USER_INVALID_USERNAME_EMPTY")
    @Size(min = 3, message = "USER_INVALID_USERNAME")
    String username;

    @NotEmpty(message = "USER_INVALID_PASSWORD_EMPTY")
    @Size(min = 8, message = "USER_INVALID_PASSWORD")
    String password;

    @NotEmpty
    String name;
    LocalDate dateOfBird;
}
