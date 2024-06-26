package com.sonfe.AppChat.config;

import com.sonfe.AppChat.entity.User;
import com.sonfe.AppChat.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sonfe.AppChat.enums.Role;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Bean
    ApplicationRunner applicationRunner (UserRepository userRepository) {
       return args -> {
           if(userRepository.findByUsername("admin").isEmpty()) {
               PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
               User userAdmin = User
                       .builder()
                       .username("admin")
                       .password(passwordEncoder.encode("Admin@123"))
                       .name("Admin")
                       .role(Role.ADMIN.name())
                       .build();
               userRepository.save(userAdmin);

               log.info("Admin user created");
           }
       };
    }
}
