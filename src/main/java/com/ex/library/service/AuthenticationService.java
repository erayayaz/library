package com.ex.library.service;

import com.ex.library.dto.AuthenticationRequest;
import com.ex.library.dto.AuthenticationResponse;
import com.ex.library.dto.RegisterRequest;
import com.ex.library.enums.Role;
import com.ex.library.exception.UsernameNotFoundException;
import com.ex.library.model.User;
import com.ex.library.model.UserRole;
import com.ex.library.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        UserRole userRole = UserRole.builder()
                .role(Role.USER)
                .build();

        var user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userService.createUser(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userService.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
