package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.AdultUrl;
import ru.kata.spring.boot_security.demo.model.User;

@Service
public class AdultServiceImp implements AdultService {
    @Override
    public AdultUrl returnAdultUrl(User user) {
        if (user.getAge() >= 18) {
            return new AdultUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", true);
        } else {
            return new AdultUrl("https://www.smeshariki.ru/", false);
        }
    }
}
