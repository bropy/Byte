package com.steam.demo.controllers;

import com.steam.demo.dto.UserDto;
import com.steam.demo.entity.User;
import com.steam.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/reg")
    public String showRegistrationForm(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "reg";
    }

    @PostMapping("/reg/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model, HttpServletResponse response){
        User existingUser = userService.findByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null, "There is already an account existed with this email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "reg";
        }

        userService.saveUser(userDto);

        Cookie userCookie = new Cookie("user", userDto.getEmail());
        userCookie.setPath("/");
        userCookie.setMaxAge(24 * 60 * 60); //1 день
        response.addCookie(userCookie);

        return "redirect:/index?success";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}