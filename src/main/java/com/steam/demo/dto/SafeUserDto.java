package com.steam.demo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SafeUserDto {
    private Long id;
    @NotEmpty
    private String login;

    @Email
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Set<SafeUserDto> friends;
}
