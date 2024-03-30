package com.karol.kindergartenmanagementsystem.filter;

import com.karol.kindergartenmanagementsystem.service.JwtService;
import com.karol.kindergartenmanagementsystem.service.UserAccountDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.AUTHORIZATION_HEADER;
import static com.karol.kindergartenmanagementsystem.http.AuthorizationHeaderProperties.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserAccountDetailsService userAccountDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
                .ifPresentOrElse(authHeader -> {
                    String token = authHeader.substring(TOKEN_PREFIX.length());
                    String email = jwtService.extractEmail(token);

                    authenticateUser(email, token, request);
                }, () -> {
                    log.info("No authorization header found or it does not start with the expected prefix.");
                });

        proceedWithFilterChain(request, response, filterChain);
    }

    private void authenticateUser(String email, String token, HttpServletRequest request) {
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userAccountDetailsService.loadUserByUsername(email);
            if (jwtService.isValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    private void proceedWithFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}