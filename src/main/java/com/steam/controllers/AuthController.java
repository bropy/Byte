package com.steam.controllers;

import com.steam.entity.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final OAuth2AuthorizedClientService authorizedClientService; // Injection

    public AuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }
    @GetMapping("/login")
    public String login() {
        return "login"; // Сторінка входу
    }

    @GetMapping("/reg")
    public String registration(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        // Отримання даних користувача з OAuth2AuthorizedClient

        // Перенаправлення на форму реєстрації
        return "reg";
    }

    @PostMapping("/reg")
    public String registerUser(@ModelAttribute User user) {
        // Збереження даних користувача в базі даних
        return "redirect:/success"; // Сторінка успішної реєстрації
    }
}