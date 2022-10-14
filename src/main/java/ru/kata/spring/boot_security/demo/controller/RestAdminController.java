package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.AdultUrl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdultServiceImp;
import ru.kata.spring.boot_security.demo.service.EmailServiceImp;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RestAdminController {
    private final UserServiceImp userService;
    private final RoleServiceImp roleService;
    private final AdultServiceImp adultService;

    @Autowired
    public RestAdminController(UserServiceImp userService, RoleServiceImp roleService, EmailServiceImp emailService, AdultServiceImp adultService) {
        this.userService = userService;
        this.roleService = roleService;
        this.adultService = adultService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUserList(100));
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping(value = "/current")
    public ResponseEntity<UserDetails> currentUser(Principal principal) {
        return ResponseEntity.ok(userService.loadUserByUsername(principal.getName()));
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

    @PostMapping(value = "registration")
    public ResponseEntity<HttpStatus> registerUser(@ModelAttribute User user, HttpServletRequest request) {
        userService.registerUser(user);
        try {
            request.login(user.getEmail(), user.getRawPassword());
        } catch (ServletException e) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/adult")
    public ResponseEntity<AdultUrl> showAdult(Principal principal) {
        return ResponseEntity.ok(adultService.returnAdultUrl(userService.getUserByName(principal.getName())));
    }


}
