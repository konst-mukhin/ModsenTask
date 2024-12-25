package org.example.bookstorageservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstorageservice.authentication.AuthenticationService;
import org.example.bookstorageservice.authentication.JwtAuthenticationResponse;
import org.example.bookstorageservice.authentication.SignInRequest;
import org.example.bookstorageservice.authentication.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(
            @RequestBody SignUpRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthenticationResponse> authenticate(
            @RequestBody SignInRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
