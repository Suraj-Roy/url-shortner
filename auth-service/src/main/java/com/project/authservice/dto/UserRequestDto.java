package com.project.authservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roles;
}
