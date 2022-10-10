package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImp userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String adminPage(ModelMap model, @RequestParam("count") Optional<Integer> count) {
        model.addAttribute("users", userService.getUserList(count.orElse(100)));
        model.addAttribute("user_model", new User());
        model.addAttribute("all_roles", roleService.getRoles());
        return "admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(ModelMap model, @PathVariable long id) {
        userService.deleteUser(id);
        model.addAttribute("users", userService.getUserList(100));
        model.addAttribute("user_model", new User());
        model.addAttribute("all_roles", roleService.getRoles());
        return "admin";
    }

    @PostMapping(value = "/add")
    public String addUser(ModelMap model, @ModelAttribute User user) {
        userService.addUser(user);
        model.addAttribute("users", userService.getUserList(100));
        model.addAttribute("user_model", new User());
        model.addAttribute("all_roles", roleService.getRoles());
        return "admin";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserPage(ModelMap model, @PathVariable long id) {
        model.addAttribute("user_for_edit", userService.getUserById(id));
        model.addAttribute("all_roles", roleService.getRoles());
        return "admin";
    }

    @PostMapping(value = "/edit")
    public String editUser(ModelMap model, @ModelAttribute User user) {
        userService.updateUser(user);
        model.addAttribute("users", userService.getUserList(100));
        model.addAttribute("user_model", new User());
        model.addAttribute("all_roles", roleService.getRoles());
        return "admin";
    }

}
