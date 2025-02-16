package org.example.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.authservice.authentication.JwtAuthenticationResponse;
import org.example.authservice.authentication.SignInRequest;
import org.example.authservice.authentication.SignUpRequest;
import org.example.authservice.exception.EntityNotFound;
import org.example.authservice.exception.UsernameAlreadyInUse;
import org.example.authservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации пользователей")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает учетную запись для нового пользователя и возвращает токен JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register (
            @RequestBody SignUpRequest request
    ) throws UsernameAlreadyInUse {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Проверяет учетные данные пользователя и возвращает токен JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован",
                            content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Некорректные учетные данные")
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthenticationResponse> authenticate(
            @RequestBody SignInRequest request
    ) throws EntityNotFound {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}



