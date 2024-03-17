package com.karol.kindergartenmanagementsystem.config;

import com.karol.kindergartenmanagementsystem.model.Role;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SeedAdminAccountConfigurationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SeedAdminAccountConfiguration seedAdminAccountConfiguration;
    @Value("${admin.password}")
    private String adminPassword;
    @Test
    public void givenSeedAdminAccount_whenAdminDoNotExist_thenCreatesAdminUser() {
        userRepository.deleteAll();

        seedAdminAccountConfiguration.run();

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());

        User admin = users.get(0);
        assertEquals("admin", admin.getFirstName());
        assertEquals("admin", admin.getLastName());
        assertEquals("admin@admin.com", admin.getEmail());
        assertEquals(Role.ADMIN, admin.getRole());
        assertTrue(passwordEncoder.matches(adminPassword, admin.getPassword()));
    }

    @Test
    public void givenSeedAdminAccount_whenAdminExists_thenDoesntCreateAdminUser() {
        seedAdminAccountConfiguration.run();

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }
}