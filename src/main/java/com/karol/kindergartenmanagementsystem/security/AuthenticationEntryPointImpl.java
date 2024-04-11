package com.karol.kindergartenmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karol.kindergartenmanagementsystem.http.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ApiErrorResponse responseMessage = ApiErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed",
                "Ensure that the username and password included in the request are correct"
        );

        log.info("Attempted unauthorized access attempted to '{}' at {}", request.getRequestURI(), LocalDateTime.now());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseMessage));
    }
}
