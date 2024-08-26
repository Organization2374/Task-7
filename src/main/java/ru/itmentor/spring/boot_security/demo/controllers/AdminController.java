package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.dto.UserResponseDTO;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserNotCreatedException;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserNotFoundException;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserNotUpdatedException;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.ErrorHandlingService;
import ru.itmentor.spring.boot_security.demo.services.RegistrationService;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final String REDIRECT_HOME = "redirect:/admin";

    private final UserService userService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final ErrorHandlingService errorHandlingService;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RegistrationService registrationService, ErrorHandlingService errorHandlingService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.errorHandlingService = errorHandlingService;
    }

    @GetMapping
    public ResponseEntity<List<User>> index() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> show(@PathVariable("id") Long id) {
        UserResponseDTO responseDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            errorHandlingService.handleError(bindingResult, new UserNotCreatedException());
        }
        registrationService.register(user);
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            errorHandlingService.handleError(bindingResult, new UserNotFoundException());
        }

        userService.updateUserFromDTO(id, userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            errorHandlingService.handleError(bindingResult, new UserNotUpdatedException());
        }
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
