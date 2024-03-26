package com.karol.kindergartenmanagementsystem.config;

import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.AUTHORIZATION_HEADER;
import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER.getValue());
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX.getValue())) {
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.getValue().length());
        Token storedToken = tokenRepository.findByToken(token).orElse(null);
        if (token != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }
}
