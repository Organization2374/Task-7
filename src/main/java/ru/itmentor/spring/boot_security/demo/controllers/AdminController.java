package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RegistrationService;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String REDIRECT_HOME = "redirect:/admin";

    private final UserService userService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RegistrationService registrationService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
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
        model.addAttribute("userDTO", userService.getUserDTOById(id));
        return "/admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        userService.updateUserFromDTO(id, userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        return REDIRECT_HOME;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return REDIRECT_HOME;
    }
}
