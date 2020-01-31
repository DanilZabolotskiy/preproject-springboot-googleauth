package ru.javamentor.preproject_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javamentor.preproject_springboot.model.Role;
import ru.javamentor.preproject_springboot.repository.RoleRepository;
import java.util.Optional;

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
}
