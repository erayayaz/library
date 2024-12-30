package com.ex.library.controller;

import com.ex.library.dto.AuthenticationRequest;
import com.ex.library.dto.AuthenticationResponse;
import com.ex.library.dto.RegisterRequest;
import com.ex.library.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(authRequest));
    }
}
