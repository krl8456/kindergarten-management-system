package com.karol.kindergartenmanagementsystem.controller;

import com.karol.kindergartenmanagementsystem.dto.AuthenticationResponse;
import com.karol.kindergartenmanagementsystem.dto.SignInRequest;
import com.karol.kindergartenmanagementsystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
