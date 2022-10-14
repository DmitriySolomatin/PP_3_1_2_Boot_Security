package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.EmailDetails;
import ru.kata.spring.boot_security.demo.model.User;

public interface EmailService {
    void sendMail(EmailDetails details);

    void sendRegistrationInfo(User user, String password);
}
