package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.AdultUrl;
import ru.kata.spring.boot_security.demo.model.User;

public interface AdultService {
    AdultUrl returnAdultUrl(User user);
}
