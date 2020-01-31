package ru.javamentor.preproject_springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class PageController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "index";
    }

    @RequestMapping(path = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
    public String getAdminPage() {
        return "admin";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "user";
    }

    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Set<String> roleNAme = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            if (roleNAme.contains("admin")) {
                return "redirect:/admin";
            } else
                return "redirect:/user";
        }
        return "redirect:/login";
    }

}
