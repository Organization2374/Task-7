package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserRegistrationException;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.ErrorHandlingService;
import ru.itmentor.spring.boot_security.demo.services.RegistrationService;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.util.UserValidator;

@Controller
public class RegistrationController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final ErrorHandlingService errorHandlingService;

    @Autowired
    public RegistrationController(UserValidator userValidator, RegistrationService registrationService, RoleService roleService, ErrorHandlingService errorHandlingService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.errorHandlingService = errorHandlingService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.findAllRoles());
        return "registration";
    }

    @ResponseBody
    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody @Valid User user,
                                              BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            errorHandlingService.handleError(bindingResult, new UserRegistrationException());
        }
        registrationService.register(user);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }

}
