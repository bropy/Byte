package com.steam.demo.controllers;

import com.steam.demo.data.CreateUserRequest;
import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.User;
import com.steam.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            User newUser = new User();
            newUser.setLogin(createUserRequest.getLogin());
            newUser.setPassword(createUserRequest.getPassword());
            newUser.setEmail(createUserRequest.getEmail());
            newUser.setFirstName(createUserRequest.getFirstName());
            newUser.setLastName(createUserRequest.getLastName());
            newUser.setBirthDate(createUserRequest.getBirthDate());

            Profile newProfile = new Profile();
            newProfile.setNickname(createUserRequest.getNickname());
            newProfile.setAvatar(createUserRequest.getAvatar());
            newProfile.setDescription(createUserRequest.getDescription());
            newProfile.setCountry(createUserRequest.getCountry());
            newProfile.setStatus(createUserRequest.getStatus());

            newUser.setProfile(newProfile);
            newProfile.setUser(newUser);

            UserDto createdUser = userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
    }
}