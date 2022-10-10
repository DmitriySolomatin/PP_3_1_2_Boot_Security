package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImp userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImp userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String adminPage(ModelMap model, @RequestParam("count") Optional<Integer> count, Principal principal) {
        model.addAttribute("users", userService.getUserList(count.orElse(100)));
        model.addAttribute("user_model", new User());
        model.addAttribute("me", userService.loadUserByUsername(principal.getName()));
        model.addAttribute("all_roles", roleService.getRoles());
        model.addAttribute("admin_role", roleService.getRoleByName("ROLE_ADMIN"));
        return "profile";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(ModelMap model, @ModelAttribute User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

    @PostMapping(value = "/add")
    public String addUser(ModelMap model, @ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/edit")
    public String editUser(ModelMap model, @ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

}
