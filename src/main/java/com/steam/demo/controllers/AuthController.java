package com.steam.demo.controllers;

import com.steam.demo.data.CreateUserRequest;
import com.steam.demo.data.LoginRequest;
import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.Profile;
import com.steam.demo.entity.User;
import com.steam.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDto authenticatedUser = userService.authenticateUser(loginRequest.getLogin(), loginRequest.getPassword());
            if (authenticatedUser != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", authenticatedUser.getId());

                String sessionToken = UUID.randomUUID().toString();
                session.setAttribute("sessionToken", sessionToken);

                // Set session cookie
                Cookie sessionCookie = new Cookie("SESSIONID", sessionToken);
                sessionCookie.setHttpOnly(true);
                sessionCookie.setSecure(request.isSecure()); // Set secure flag if using HTTPS
                sessionCookie.setPath("/");
                sessionCookie.setMaxAge(3600); // 1 hour
                response.addCookie(sessionCookie);

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("user", authenticatedUser);
                responseBody.put("message", "Login successful");

                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            String sessionToken = (String) session.getAttribute("sessionToken");
            String cookieToken = getSessionTokenFromCookie(request);

            if (userId != null && sessionToken != null && sessionToken.equals(cookieToken)) {
                try {
                    UserDto currentUser = userService.getUserDtoById(userId);
                    if (currentUser != null) {
                        return ResponseEntity.ok(currentUser);
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error retrieving user data: " + e.getMessage());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear session cookie
        Cookie sessionCookie = new Cookie("SESSIONID", null);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(request.isSecure());
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        return ResponseEntity.ok().body("Logged out successfully");
    }

    private String getSessionTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}