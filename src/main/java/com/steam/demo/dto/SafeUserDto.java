package com.steam.demo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
