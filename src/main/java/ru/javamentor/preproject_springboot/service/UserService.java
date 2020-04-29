package ru.javamentor.preproject_springboot.service;

import ru.javamentor.preproject_springboot.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUserById(long id);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByGoogleId(String id);

}
