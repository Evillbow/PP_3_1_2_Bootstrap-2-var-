package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.web.dto.UserForm;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import javax.validation.Valid;
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
        model.addAttribute("userForm", new UserForm());

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
        model.addAttribute("userForm", new UserForm());

        return "admin";
    }



    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("userForm") UserForm form,
                          BindingResult br,
                          RedirectAttributes ra) {

        if (form.getPassword() == null || form.getPassword().isBlank()) {
            br.rejectValue("password", "password.blank", "Password is required");
        }

        if (br.hasErrors()) {
            ra.addFlashAttribute("errorsAdd", br.getAllErrors());
            return "redirect:/admin#newUser";
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user.setYear(form.getYear());

        Set<String> roles = form.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of("ROLE_USER");
        }

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
    public String updateUser(@Valid @ModelAttribute("userForm") UserForm form,
                             BindingResult br,
                             RedirectAttributes ra) {

        if (form.getId() == null) {
            br.rejectValue("id", "id.null", "User id is required");
        }

        if (br.hasErrors()) {
            ra.addFlashAttribute("errorsEdit", br.getAllErrors());
            return "redirect:/admin";
        }

        User user = new User();
        user.setId(form.getId());
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user.setYear(form.getYear());

        Set<String> roles = form.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of("ROLE_USER");
        }

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

