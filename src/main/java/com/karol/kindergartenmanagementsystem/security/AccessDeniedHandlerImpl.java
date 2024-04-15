package com.karol.kindergartenmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karol.kindergartenmanagementsystem.http.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ApiErrorResponse responseMessage = ApiErrorResponse.of(
                HttpStatus.FORBIDDEN.value(),
                "Access denied",
                "You do not have the necessary permissions to access this resource."
        );

        log.info("Access denied for user: {} trying to access: {} at {}", request.getRemoteUser(), request.getRequestURI(), LocalDateTime.now());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseMessage));
    }
}

