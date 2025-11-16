package ru.gavrilovegor519.tasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gavrilovegor519.tasks.dto.input.users.LoginDto;
import ru.gavrilovegor519.tasks.dto.input.users.RegDto;
import ru.gavrilovegor519.tasks.dto.output.Response;
import ru.gavrilovegor519.tasks.dto.output.users.TokenDto;
import ru.gavrilovegor519.tasks.entity.User;
import ru.gavrilovegor519.tasks.mapper.UserMapper;
import ru.gavrilovegor519.tasks.service.UserService;

@RestController
@RequestMapping("/api/1.0/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper registrationDataInputMapper;

    @PostMapping("/login")
    @Operation(summary = "Get JWT token by login and password",
                responses = {
                        @ApiResponse(description = "JWT token for user",
                                useReturnTypeSchema = true),
                        @ApiResponse(responseCode = "403",
                                description = "User is not found or incorrect password")
                })
    public ResponseEntity<Response<TokenDto>> login(@Parameter(description = "Login data", required = true)
                            @RequestBody @Valid LoginDto loginDto) {
        User user = registrationDataInputMapper.map(loginDto);
        TokenDto tokenDto = userService.login(user);

        return ResponseEntity.ok(new Response<>(tokenDto, "Login successful", true));
    }

    @PostMapping("/reg")
    @Operation(summary = "Register the user for using this service",
                responses = {
                        @ApiResponse(description = "User is registered",
                                responseCode = "200"),
                        @ApiResponse(responseCode = "409",
                                description = "Duplicate registration data")
                })
    public ResponseEntity<Response<String>> reg(@Parameter(description = "Registration data", required = true)
                        @RequestBody @Valid RegDto regDto) {
        User user = registrationDataInputMapper.map(regDto);
        userService.reg(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>("User registered successfully", true));
    }

}
