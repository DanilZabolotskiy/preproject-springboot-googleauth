package ru.javamentor.preproject_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.preproject_springboot.model.Role;
import ru.javamentor.preproject_springboot.model.User;
import ru.javamentor.preproject_springboot.service.RoleService;
import ru.javamentor.preproject_springboot.service.UserService;
import ru.javamentor.preproject_springboot.util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService service, RoleService roleService) {
        this.userService = service;
        this.roleService = roleService;
    }

    @GetMapping("/admin/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "/admin/user")
    public User getUserById(@RequestParam(value = "login")String login){
        return userService.getUserByLogin(login).get();
    }

    @DeleteMapping(path = "/admin/user")
    public void deleteUserById(@RequestParam(value = "id")Long id){
        userService.deleteUserById(id);
    }

    @PutMapping("/admin/user")
    public void updateUser(User user,
                           @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                           @RequestParam(value = "roleUser", required = false) String roleUser){
        Set<Role> roles = roleService.findRolesByName(roleAdmin, roleUser);
        user.setRoles(roles);
        userService.updateUser(user);
    }

    @PostMapping("/admin/user")
    public void addUser(User user,
                          @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                          @RequestParam(value = "roleUser", required = false) String roleUser) {

        Set<Role> roles = roleService.findRolesByName(roleAdmin, roleUser);
        user.setRoles(roles);
        userService.addUser(user);
    }

    @GetMapping("/get_auth_user")
    public User getAuthUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
