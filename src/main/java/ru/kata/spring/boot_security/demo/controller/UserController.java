package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    // ты тут используешь принципала , а зачем тогда в бд идешь? в нем достаточно данных для данной страницы
    // - Так в Principal нет ни возраста, ни фамилии. Только имя.
    @GetMapping(value = "")
    public String userPage(ModelMap model, Principal principal) {
        model.addAttribute("me", userService.loadUserByUsername(principal.getName()));
        return "index";
    }


}