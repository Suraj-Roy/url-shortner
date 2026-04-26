package com.project.authservice.controller;

import com.project.authservice.dto.JwtTokenResponse;
import com.project.authservice.dto.LoginRequest;
import com.project.authservice.dto.UserDto;
import com.project.authservice.dto.UserRequestDto;
import com.project.authservice.exception.BadRequestException;
import com.project.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register-user")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRequestDto user) {
        UserDto userDto = userService.saveUser(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/generate-token")
    public ResponseEntity<JwtTokenResponse> generateToken(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(
                userService.generateToken(authentication.getName(), roles)
        );
    }
}
