package com.karol.kindergartenmanagementsystem.repository;

import com.karol.kindergartenmanagementsystem.model.Role;
import com.karol.kindergartenmanagementsystem.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUser_whenSave_thenReturnsSavedUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);
    }

    @Test
    public void givenUsers_whenFindAll_thenReturnsCorrectAmountOfUsers() {
        User user1 = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        User user2 = User.builder()
                .firstName("test2")
                .lastName("test2")
                .email("email2@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> savedUsers = userRepository.findAll();

        assertNotNull(savedUsers);
        assertEquals(2, savedUsers.size());
    }

    @Test
    public void givenUser_whenFindById_thenReturnsUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        userRepository.save(user);

        User retrievedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertNotNull(retrievedUser);
    }

    @Test
    public void givenUser_whenFindByEmail_thenReturnsUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        userRepository.save(user);

        User retrievedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertNotNull(retrievedUser);
    }

    @Test
    public void givenUser_whenUpdate_thenReturnsUpdatedUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        userRepository.save(user);

        User retrievedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new AssertionError("User not found"));
        retrievedUser.setEmail("differentEmail@gmail.com");
        retrievedUser.setFirstName("newTest");
        User updatedUser = userRepository.save(retrievedUser);

        assertNotNull(updatedUser);
        assertNotNull(updatedUser.getEmail());
        assertNotNull(updatedUser.getFirstName());
    }

    @Test
    public void givenUser_whenDelete_thenDeletesUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> removalReturn = userRepository.findById(user.getId());

        assertTrue(removalReturn.isEmpty());
    }
}