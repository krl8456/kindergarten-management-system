package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.model.AuthenticationResponse;
import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Account with this email not found"));

        String token = jwtService.generateToken(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token);
    }

    private void saveUserToken(String token, User user) {
        Token tokenWithStatus = new Token();
        tokenWithStatus.setToken(token);
        tokenWithStatus.setLoggedOut(false);
        tokenWithStatus.setUser(user);
        tokenRepository.save(tokenWithStatus);
    }
}
