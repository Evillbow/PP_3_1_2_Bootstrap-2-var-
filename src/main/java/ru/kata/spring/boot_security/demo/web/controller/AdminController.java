package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminHome(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("activePage", "admin");
        model.addAttribute("rolesText", rolesText());
        model.addAttribute("roleAdmin", userService.getRoleByName("ROLE_ADMIN"));
        model.addAttribute("roleUser", userService.getRoleByName("ROLE_USER"));

        return "admin";
    }

    // Можно оставить, но редиректы лучше вести на /admin
    @GetMapping("/users")
    public String users(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("activePage", "admin");
        model.addAttribute("rolesText", rolesText());
        model.addAttribute("roleAdmin", userService.getRoleByName("ROLE_ADMIN"));
        model.addAttribute("roleUser", userService.getRoleByName("ROLE_USER"));

        return "admin";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("name") String name,
                          @RequestParam(value = "surname", required = false) String surname,
                          @RequestParam(value = "year", required = false) Integer year,
                          @RequestParam(value = "roles", required = false) Set<String> roles) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setYear(year);

        // вызываем перегруженный метод
        userService.addUser(user, roles);

        return "redirect:/admin";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    // если edit.html ты сейчас не используешь (у тебя модалки), этот метод можно вообще не трогать
    @GetMapping("/users/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("username") String username,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam("name") String name,
                             @RequestParam(value = "surname", required = false) String surname,
                             @RequestParam(value = "year", required = false) Integer year,
                             @RequestParam(value = "roles", required = false) Set<String> roles) {

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setYear(year);

        // вызываем перегруженный метод
        userService.updateUser(user, roles);

        return "redirect:/admin";
    }

    private String rolesText() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .collect(Collectors.joining(" "));
    }
}

