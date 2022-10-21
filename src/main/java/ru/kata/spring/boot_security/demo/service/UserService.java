package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(long id);

    List<User> getUserList(int count);

    void deleteUser(User user);

    void addUser(User user);

    void registerUser(User user);

    void updateUser(User user);

    void cryptPassword(User user);

    void addRole(User user, String roleName);

    User getUserByName(String name);

}
