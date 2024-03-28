package com.karol.kindergartenmanagementsystem.service;

import com.karol.kindergartenmanagementsystem.dto.AuthenticationResponse;
import com.karol.kindergartenmanagementsystem.dto.SignInRequest;
import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.model.User;
import com.karol.kindergartenmanagementsystem.repository.TokenRepository;
import com.karol.kindergartenmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse authenticate(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Account with this email not found"));

        String token = jwtService.generateToken(user);
        revokeAllTokensByUser(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token);
    }

    private void revokeAllTokensByUser(User user) {
        List<Token> validTokensByUser = tokenRepository.findAllTokensByUser(user.getId());
        if(!validTokensByUser.isEmpty()) {
            validTokensByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokensByUser);
    }

    private void saveUserToken(String token, User user) {
        Token tokenWithStatus = new Token();
        tokenWithStatus.setToken(token);
        tokenWithStatus.setLoggedOut(false);
        tokenWithStatus.setUser(user);
        tokenRepository.save(tokenWithStatus);
    }
}
