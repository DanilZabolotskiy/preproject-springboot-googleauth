package ru.javamentor.preproject_springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javamentor.preproject_springboot.model.Role;
import ru.javamentor.preproject_springboot.repository.RoleRepository;
import ru.javamentor.preproject_springboot.service.RoleService;

import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return Optional.ofNullable(roleRepository.findRoleByName(roleName));
    }

    @Override
    public Set<Role> findRolesByName(String... roleName) {
        return roleRepository.findRolesByName(roleName);
    }

}
