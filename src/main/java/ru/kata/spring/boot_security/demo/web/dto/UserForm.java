package ru.kata.spring.boot_security.demo.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class UserForm {

    private Long id;

    @NotBlank(message = "Email is required")
    private String username;

    // На ADD обязателен, на UPDATE может быть пустой — поэтому НЕ ставим @NotBlank здесь.
    private String password;

    @NotBlank(message = "First name is required")
    private String name;

    private String surname;

    @Min(value = 0, message = "Age must be >= 0")
    private Integer year;

    private Set<String> roles;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}

