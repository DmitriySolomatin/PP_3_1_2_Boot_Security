package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User getUserById(long id);

    List<User> getUserList(int count);

    void deleteUser(long id);

    void addUser(User user);

    void updateUser(User user);

    void cryptPassword(User user);

}
