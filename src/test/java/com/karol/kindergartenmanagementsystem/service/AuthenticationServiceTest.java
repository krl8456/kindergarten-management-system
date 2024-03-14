package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.dto.AuthenticationResponse;
import com.karol.kindergartenmanagementsystem.dto.SignInRequest;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void givenUser_whenAuthenticate_thenReturnsToken() {
        String email = "test@example.com";
        String password = "password";
        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        SignInRequest request = SignInRequest.builder()
                .email(email)
                .password(password)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthenticationResponse response = authenticationService.authenticate(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void givenNotCreatedUser_whenAuthenticate_thenThrowsAnException() {
        String email = "test@example.com";
        String password = "password";
        SignInRequest request = SignInRequest.builder()
                .email(email)
                .password(password)
                .build();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(request);
        });
    }
}