package com.example.Edafon_Authentication.controllers;

import com.example.Edafon_Authentication.Service.LoginPerrsonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final LoginPerrsonService loginService;

    public AuthController (LoginPerrsonService loginService)
    {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String loginGet()
    {
        return "authentication/login.html";
    }

    @PostMapping("/")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    )
    {

        loginService.setUsername(username);
        loginService.setPassword(password);

        boolean isLogin = loginService.login();

        if (isLogin)
        {
            model.addAttribute("massage", "Успешный вход! ");
            return "redirect:/main";
        }else
        {
            model.addAttribute( "massage", "Введены неверные данные");
            return "authentication/login.html";
        }
    }
}
