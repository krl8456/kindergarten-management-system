package com.karol.kindergartenmanagementsystem.security;

import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Value("${authorization.header}")
    private String AuthorizationHeader;
    @Value("${jwt.token.prefix}")
    private String JwtTokenPrefix;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        Optional.ofNullable(request.getHeader(AuthorizationHeader))
                .filter(authHeader -> authHeader.startsWith(JwtTokenPrefix))
                .ifPresentOrElse(authHeader -> {
                    String token = authHeader.substring(JwtTokenPrefix.length());
                    revokeToken(token);
                }, () -> log.info("No authorization header found or it does not start with the expected prefix."));
    }

    private void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresentOrElse(storedToken -> {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }, () -> log.warn("No token found for logout: {}", token));
    }
}

