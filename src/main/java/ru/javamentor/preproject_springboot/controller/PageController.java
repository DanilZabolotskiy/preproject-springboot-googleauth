package ru.javamentor.preproject_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "index";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "user";
    }

}
