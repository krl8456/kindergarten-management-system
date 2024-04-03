package com.karol.kindergartenmanagementsystem.init;

import com.karol.kindergartenmanagementsystem.model.Role;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import com.karol.kindergartenmanagementsystem.service.UserAccountDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:admin.properties")
public class AdminAccountSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountDetailsService userAccountDetailsService;
    @Value("${firstname}")
    private String firstName;
    @Value("${lastname}")
    private String lastName;
    @Value("${phoneNumber}")
    private String phoneNumber;
    @Value("${email}")
    private String email;
    @Value("${password}")
    private String password;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User
                    .builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(phoneNumber)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .build();
            userAccountDetailsService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }
}
