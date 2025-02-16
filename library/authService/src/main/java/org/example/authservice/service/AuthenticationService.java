package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.authentication.JwtAuthenticationResponse;
import org.example.authservice.authentication.JwtService;
import org.example.authservice.authentication.SignInRequest;
import org.example.authservice.authentication.SignUpRequest;
import org.example.authservice.exception.EntityNotFound;
import org.example.authservice.exception.UsernameAlreadyInUse;
import org.example.authservice.model.Role;
import org.example.authservice.model.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse register(SignUpRequest signUpRequest) throws UsernameAlreadyInUse {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.valueOf(signUpRequest.getRole()))
                .build();
        if(userRepository.findByUsername(signUpRequest.getUsername()).isPresent()){
            throw new UsernameAlreadyInUse("Username is already in use");
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }

    public JwtAuthenticationResponse authenticate(SignInRequest signInRequest) throws EntityNotFound {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        }
        catch (Exception e){
            throw new EntityNotFound("User not found");
        }
        var user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }
}

