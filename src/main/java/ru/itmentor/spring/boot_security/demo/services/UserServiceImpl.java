package ru.itmentor.spring.boot_security.demo.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void update(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @Transactional
    public void updateUserFromDTO(Long id, UserDTO userDTO, BindingResult bindingResult) {
        User user = userRepository.findById(id);

        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "", "Current password is incorrect");
            return;
        }
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getNewPassword() != null && !userDTO.getCurrentPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public UserDTO getUserDTOById(Long id) {
        User user = userRepository.findById(id);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }
}
