package ru.javamentor.preproject_springboot.service;

import ru.javamentor.preproject_springboot.model.Role;
import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Optional<Role> getRoleByName(String roleName);

    Set<Role> findRolesByName(String... roleName);

}
