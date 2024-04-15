package com.karol.kindergartenmanagementsystem.config;

import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import com.karol.kindergartenmanagementsystem.security.CustomLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CustomLogoutHandlerTest {
    @Autowired
    private TokenRepository tokenRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Autowired
    private CustomLogoutHandler customLogoutHandler;
    @Value("${authorization.header}")
    private String AuthorizationHeader;
    @Value("${jwt.token.prefix}")
    private String JwtTokenPrefix;

    @Test
    public void givenToken_whenLogout_thenSetsTokenToLoggedOut() {
        Token token = Token.builder()
                .loggedOut(false)
                .token("token")
                .build();
        tokenRepository.save(token);
        when(request.getHeader(AuthorizationHeader)).thenReturn(JwtTokenPrefix + token.getToken());

        customLogoutHandler.logout(request, response, authentication);

        Token retrievedToken = tokenRepository.findByToken(token.getToken())
                .orElseThrow(() -> new AssertionError("Token not found"));

        assertNotNull(retrievedToken);
        assertTrue(retrievedToken.isLoggedOut());
    }
}