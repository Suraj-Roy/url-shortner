package com.project.authservice.service;

import com.project.authservice.dto.JwtTokenResponse;
import com.project.authservice.dto.UserDto;
import com.project.authservice.dto.UserRequestDto;
import com.project.authservice.entity.User;
import com.project.authservice.exception.HandleUserAlreadyExists;
import com.project.authservice.repository.UserRepository;
import com.project.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto saveUser(UserRequestDto user){

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new HandleUserAlreadyExists("User already exists");
        }
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles())
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(newUser);

        return UserDto.builder().id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles())
                .build();
    }

    public JwtTokenResponse generateToken(String username,List<String> roles) {
        String token = jwtUtil.generateToken(username,roles);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setToken(token);
        jwtTokenResponse.setType("Bearer");
        jwtTokenResponse.setValidUntil(jwtUtil.extractExpiration(token).toString());
        return jwtTokenResponse;
    }
}
