package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDuplicateEmail(DataIntegrityViolationException ex, RedirectAttributes ra) {
        // чаще всего это unique constraint на username/email
        ra.addFlashAttribute("errorsAdd", java.util.List.of(
                new org.springframework.validation.ObjectError("username",
                        "Email already exists")
        ));
        return "redirect:/admin#newUser";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("errorsAdd", java.util.List.of(
                new org.springframework.validation.ObjectError("app",
                        ex.getMessage())
        ));
        return "redirect:/admin";
    }
}

