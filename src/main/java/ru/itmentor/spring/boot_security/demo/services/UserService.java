package ru.itmentor.spring.boot_security.demo.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void update(Long id, User user);
    void delete(Long id);
    User findByName(String name);

}
