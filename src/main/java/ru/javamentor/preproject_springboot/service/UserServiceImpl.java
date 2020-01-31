package ru.javamentor.preproject_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javamentor.preproject_springboot.model.User;
import ru.javamentor.preproject_springboot.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean addUser(User user) {
        String simplePassword = user.getPassword();
        String encodePassword = passwordEncoder.encode(simplePassword);
        user.setPassword(encodePassword);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteUserById(long id) {
        userRepository.deleteById(id);
        return true;
    }

   @Override
    public Optional<User> getUserByLogin(String login) {
       return Optional.ofNullable(userRepository.findUserByLogin(login));
    }

    @Override
    public Optional<User> getUserByGoogleId(String id) {
        return Optional.ofNullable(userRepository.findUserByGoogleID(id));
    }

}
