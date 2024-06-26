package com.sonfe.AppChat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBird;
    private String role;
    private Date createdDate;
    private String createdBy;
}
