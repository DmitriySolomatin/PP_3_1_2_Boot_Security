package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;
    private final RoleService roleService;

    @Autowired
    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, EmailService emailService1, RoleService roleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService1;
        this.roleService = roleService;
    }

    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    public List<User> getUserList(int count) {
        return userRepository.findAll(PageRequest.of(0, count)).stream().toList();
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void addUser(User user) {
        cryptPassword(user);
        userRepository.save(user);
    }

    @Override
    public void registerUser(User user) {
        addRole(user, "ROLE_USER");
        user.setRawPassword(user.getPassword());
        addUser(user);
        emailService.sendRegistrationInfo(user, user.getRawPassword());
    }

    @Transactional
    public void updateUser(User user) {
        cryptPassword(user);

        if (user.getPassword().isEmpty()) {
            user.setPassword(userRepository.getById(user.getId()).getPassword());
        }

        userRepository.save(user);
    }

    @Override
    public void cryptPassword(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
    }

    @Override
    public void addRole(User user, String roleName) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName(roleName));
        user.setRoles(roles);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameWithRoles(username);
    }

    public User getUserByName(String name) {
        return userRepository.findByUsernameWithRoles(name);
    }
}
