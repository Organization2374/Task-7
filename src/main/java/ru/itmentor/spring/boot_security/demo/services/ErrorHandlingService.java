package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserCustomException;

@Service
public class ErrorHandlingService {

    public String handleError(BindingResult bindingResult, UserCustomException e) {
        StringBuilder errorMsg = new StringBuilder();
        bindingResult.getFieldErrors()
                .forEach(error -> errorMsg
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";")
                );
        e.setMessage(errorMsg.toString());
        throw e;
    }
}
