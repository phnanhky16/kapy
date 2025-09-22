package com.KapybaraWeb.kapyweb.config;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.enums.Role;
import com.KapybaraWeb.kapyweb.repository.UserRepository;

@Configuration
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            // Initialize the database with a default user
            if (!userRepository.existsByUsername("admin")) {
                User defaultUser = new User();
                defaultUser.setUsername("admin");

                defaultUser.setPassword(passwordEncoder.encode("admin"));
                defaultUser.setBirthday(LocalDate.of(2023, 9, 25));
                defaultUser.setCreatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                defaultUser.setRole(Role.ADMIN);
                defaultUser.setStatus(true);
                userRepository.save(defaultUser);
            }
        };
    }
}
