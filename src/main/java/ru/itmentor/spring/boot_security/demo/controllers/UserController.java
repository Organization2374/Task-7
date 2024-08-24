package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmentor.spring.boot_security.demo.models.User;

@Controller
public class UserController {

    @GetMapping("/user")
    public String show(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "/user";
    }
}
