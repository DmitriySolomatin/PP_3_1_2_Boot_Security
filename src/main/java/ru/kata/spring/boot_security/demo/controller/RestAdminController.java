package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RestAdminController {
    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @Autowired
    public RestAdminController(UserServiceImp userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return userService.getUserList(100);
    }

    @GetMapping(value = "/roles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping(value = "/current")
    public UserDetails currentUser(Principal principal) {
        return userService.loadUserByUsername(principal.getName());
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<HttpStatus> deleteUser(@ModelAttribute User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<HttpStatus> addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<HttpStatus> editUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
