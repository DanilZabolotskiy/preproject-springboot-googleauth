package ru.javamentor.preproject_springboot.service;

import ru.javamentor.preproject_springboot.model.Role;
import java.util.Optional;

public interface RoleService {
    Optional<Role> getRoleByName(String roleName);
}
