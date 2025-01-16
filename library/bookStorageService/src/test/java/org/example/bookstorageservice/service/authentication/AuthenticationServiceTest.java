package org.example.bookstorageservice.service.authentication;

import org.example.bookstorageservice.authentication.JwtAuthenticationResponse;
import org.example.bookstorageservice.authentication.JwtService;
import org.example.bookstorageservice.authentication.SignInRequest;
import org.example.bookstorageservice.authentication.SignUpRequest;
import org.example.bookstorageservice.exception.BadRequest;
import org.example.bookstorageservice.model.Role;
import org.example.bookstorageservice.model.User;
import org.example.bookstorageservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() throws BadRequest {
        SignUpRequest signUpRequest = new SignUpRequest("testuser", "password", "USER");
        User user = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(signUpRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.register(signUpRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_BadRequest() {
        SignUpRequest signUpRequest = new SignUpRequest("testuser", "password", "USER");

        when(userRepository.findByUsername(signUpRequest.getUsername())).thenReturn(Optional.of(new User()));

        BadRequest exception = assertThrows(BadRequest.class, () -> authenticationService.register(signUpRequest));
        assertEquals("Username is already in use", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_Success() {
        SignInRequest signInRequest = new SignInRequest("testuser", "password");
        User user = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.authenticate(signInRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );
    }
}
