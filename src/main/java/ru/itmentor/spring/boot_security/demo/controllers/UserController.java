package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RegistrationService;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class UserController {

    private static final String REDIRECT_HOME = "redirect:/admin";

    private final UserService userService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, RegistrationService registrationService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "/admin/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "/admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        registrationService.register(user);
        return REDIRECT_HOME;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        return "/admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        User existingUser = userService.findById(id);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        userService.update(id, user);
        return REDIRECT_HOME;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return REDIRECT_HOME;
    }
}
