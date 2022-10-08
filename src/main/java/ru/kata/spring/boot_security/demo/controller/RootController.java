package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class RootController {
    @GetMapping(value = "/")
    public String indexPage(Principal principal) {
        return "index";
    }
}
