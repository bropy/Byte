package com.steam.demo.controllers;

import com.steam.demo.data.CreateUserRequest;
import com.steam.demo.data.LoginRequest;
import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.User;
import com.steam.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")//тут юзаємо сесії для авторизації
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            UserDto authenticatedUser = userService.authenticateUser(loginRequest.getLogin(), loginRequest.getPassword());
            if (authenticatedUser != null) {
                session.setAttribute("userId", authenticatedUser.getId());
                String sessionToken = java.util.UUID.randomUUID().toString();
                session.setAttribute("sessionToken", sessionToken);
                return ResponseEntity.ok()
                        .header("Set-Cookie", "sessionToken=" + sessionToken + "; HttpOnly; SameSite=Strict")
                        .body(authenticatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Logged out successfully");
    }
}