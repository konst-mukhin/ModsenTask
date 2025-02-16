package org.example.authservice.service;

import org.example.authservice.authentication.JwtAuthenticationResponse;
import org.example.authservice.authentication.JwtService;
import org.example.authservice.authentication.SignInRequest;
import org.example.authservice.authentication.SignUpRequest;
import org.example.authservice.exception.EntityNotFound;
import org.example.authservice.exception.UsernameAlreadyInUse;
import org.example.authservice.model.Role;
import org.example.authservice.model.User;
import org.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");
        signUpRequest.setRole("USER");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password");

        user = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_ReturnJwtResponse() throws UsernameAlreadyInUse {
        when(userRepository.findByUsername(signUpRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        JwtAuthenticationResponse response = authenticationService.register(signUpRequest);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_ReturnJwtResponse() throws EntityNotFound {
        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        JwtAuthenticationResponse response = authenticationService.authenticate(signInRequest);

        assertNotNull(response);
        assertEquals("token", response.getToken());
    }
}
