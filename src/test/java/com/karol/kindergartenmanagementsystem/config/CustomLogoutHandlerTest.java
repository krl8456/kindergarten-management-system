package com.karol.kindergartenmanagementsystem.config;

import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import com.karol.kindergartenmanagementsystem.security.CustomLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.AUTHORIZATION_HEADER;
import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.TOKEN_PREFIX;

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
    @Test
    public void givenToken_whenLogout_thenSetsTokenToLoggedOut() throws Exception {
        Token token = Token.builder()
                .loggedOut(false)
                .token("token")
                .build();
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(TOKEN_PREFIX + token.getToken());
        when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

        customLogoutHandler.logout(request, response, authentication);

        assertTrue(token.isLoggedOut());
    }
}