package com.steam.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String githubUsername = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("login");
            model.addAttribute("githubUsername", githubUsername);
        }
        return "index";
    }

    @GetMapping("/success")
    public String success() {
        return "redirect:/"; // Перенаправити на головну після успішного входу
    }
}
