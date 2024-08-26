package ru.itmentor.spring.boot_security.demo.services;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.dto.UserResponseDTO;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exceptions.UserNotFoundException;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
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
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "", "Current password is incorrect");
            return;
        }
        modelMapper.map(userDTO, user);

        if (userDTO.getNewPassword() != null && !userDTO.getCurrentPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public UserResponseDTO getUserDTOById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserResponseDTO.class);
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
