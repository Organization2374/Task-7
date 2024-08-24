package ru.itmentor.spring.boot_security.demo.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void update(Long id, User user);
    void delete(Long id);
    User findByName(String name);
    void updateUserFromDTO(Long id, UserDTO userDTO, BindingResult bindingResult);
    UserDTO getUserDTOById(Long id);

}
