package org.example.bookstorageservice.service.authentication;

import lombok.RequiredArgsConstructor;
import org.example.bookstorageservice.authentication.JwtAuthenticationResponse;
import org.example.bookstorageservice.authentication.JwtService;
import org.example.bookstorageservice.authentication.SignInRequest;
import org.example.bookstorageservice.authentication.SignUpRequest;
import org.example.bookstorageservice.exception.BadRequest;
import org.example.bookstorageservice.exception.Unauthorized;
import org.example.bookstorageservice.model.Role;
import org.example.bookstorageservice.model.User;
import org.example.bookstorageservice.repository.UserRepository;
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

    public JwtAuthenticationResponse register(SignUpRequest signUpRequest) throws BadRequest {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.valueOf(signUpRequest.getRole()))
                .build();
        if(userRepository.findByUsername(signUpRequest.getUsername()).isPresent()){
            throw new BadRequest("Username is already in use");
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }

    public JwtAuthenticationResponse authenticate(SignInRequest signInRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }
}
