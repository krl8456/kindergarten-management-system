package com.karol.kindergartenmanagementsystem.config;

import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLogoutHandlerTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void givenToken_whenLogout_thenSetsTokenToLoggedOut() throws Exception {
        Token token = Token.builder()
                .loggedOut(false)
                .token("token")
                .build();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getToken());
        when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

        customLogoutHandler.logout(request, response, authentication);

        assertTrue(token.isLoggedOut());
    }
}