package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserAccountDetailsService userAccountDetailsService;

    @Test
    public void givenUsersEmail_whenLoadingByUsername_thenReturnsSavedUser() {
        User user = User.builder()
                .email("test@example.com")
                .build();
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails retrievedUser = userAccountDetailsService.loadUserByUsername(user.getEmail());

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(retrievedUser.getUsername(), user.getUsername());
    }

    @Test
    public void givenNonExistentUser_whenLoadingByUsername_thenThrowsAnException() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userAccountDetailsService.loadUserByUsername(email));
    }

    @Test
    public void givenNotCreatedUser_whenSave_thenSetsCreatedAtAndUpdatedAt() {
        User user = new User();

        userAccountDetailsService.save(user);

        Assertions.assertNotNull(user.getCreatedAt());
        Assertions.assertNotNull(user.getUpdatedAt());
    }

    @Test
    public void givenCreatedUser_whenSave_thenSetsOnlyUpdatedAt() {
        User user = User.builder()
                .id(1)
                .build();

        userAccountDetailsService.save(user);

        Assertions.assertNull(user.getCreatedAt());
        Assertions.assertNotNull(user.getUpdatedAt());
    }
}