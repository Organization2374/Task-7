package ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions;

public class UserNotFoundException extends UserCustomException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
