package com.karol.kindergartenmanagementsystem.init;

import com.karol.kindergartenmanagementsystem.model.Role;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import com.karol.kindergartenmanagementsystem.service.UserAccountDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountDetailsService userAccountDetailsService;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User
                    .builder()
                    .firstName("admin")
                    .lastName("admin")
                    .phoneNumber("123456789")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();
            userAccountDetailsService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }
}
