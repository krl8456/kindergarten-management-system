package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/application.properties"));
        String secretKey = properties.getProperty("jwt.secret");
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", secretKey);
    }

    @Test
    public void givenUser_whenGeneratingToken_thenReturnsToken() {
        User user = User.builder()
                .email("test@example.com")
                .build();

        String token = jwtService.generateToken(user);

        Assertions.assertNotNull(token);
    }

    @Test
    public void givenToken_whenExtractingEmail_thenReturnsEmail() {
        String expectedEmail = "test@example.com";
        User user = User.builder()
                .email(expectedEmail)
                .build();
        String token = jwtService.generateToken(user);

        String retrievedEmailFromToken = jwtService.extractEmail(token);

        Assertions.assertEquals(expectedEmail, retrievedEmailFromToken);
    }

    @Test
    public void givenToken_whenExtractingEmailFromClaims_thenReturnsEmail() {
        String expectedEmail = "test@example.com";
        User user = User.builder()
                .email(expectedEmail)
                .build();
        String token = jwtService.generateToken(user);

        String retrievedEmail = jwtService.extractClaim(token, Claims::getSubject);

        Assertions.assertEquals(expectedEmail, retrievedEmail);
    }

    @Test
    public void givenToken_whenExtractingExpirationDateFromClaims_thenReturnsExpirationDate() {
        User user = User.builder()
                .email("test@example.com")
                .build();
        String token = jwtService.generateToken(user);

        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        Assertions.assertNotNull(expirationDate);
    }

    @Test
    public void givenValidToken_whenCheckingItsValidity_thenReturnsTrue() {
        User user = User.builder()
                .email("test@example.com")
                .build();
        String generatedToken = jwtService.generateToken(user);
        when(tokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(new Token(1, generatedToken, false, user)));

        boolean isTokenValid = jwtService.isValid(generatedToken, user);

        Assertions.assertTrue(isTokenValid);
    }

    @Test
    public void givenTokenNotFoundInRepository_whenCheckingItsValidity_thenReturnsFalse() {
        User user = User.builder()
                .email("test@example.com")
                .build();
        String token = jwtService.generateToken(user);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        boolean isTokenValid = jwtService.isValid(token, user);

        Assertions.assertFalse(isTokenValid);
    }

    @Test
    public void givenTokenLoggedOutInRepository_whenCheckingItsValidity_thenReturnsFalse() {
        User user = User.builder()
                .email("test@example.com")
                .build();
        String token = jwtService.generateToken(user);
        when(tokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(new Token(1, token, true, user)));

        boolean isTokenValid = jwtService.isValid(token, user);

        Assertions.assertFalse(isTokenValid);
    }

    @Test
    public void givenTokenWithEmailMismatch_whenCheckingItsValidity_thenReturnsFalse() {
        User user = Mockito.mock(User.class);
        when(user.getEmail()).thenReturn("test@example.com");
        String token = jwtService.generateToken(user);
        when(user.getUsername()).thenReturn("different@example.com");

        boolean isTokenValid = jwtService.isValid(token, user);

        Assertions.assertFalse(isTokenValid);
    }
}
