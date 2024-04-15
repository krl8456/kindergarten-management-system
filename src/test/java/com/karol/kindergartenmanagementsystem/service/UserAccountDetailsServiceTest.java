package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        assertNotNull(retrievedUser);
        assertEquals(retrievedUser.getUsername(), user.getUsername());
    }

    @Test
    public void givenNonExistentUser_whenLoadingByUsername_thenThrowsAnException() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userAccountDetailsService.loadUserByUsername(email));
    }
}