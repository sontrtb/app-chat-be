package com.sonfe.AppChat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBird;
    private String role;
}
