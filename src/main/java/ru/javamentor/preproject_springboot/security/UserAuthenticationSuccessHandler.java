package ru.javamentor.preproject_springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.javamentor.preproject_springboot.model.Role;
import ru.javamentor.preproject_springboot.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Set<Role> roles = authUser.getRoles();
        Role adminRole = new Role("admin");
        Role userRole = new Role("user");
        if(roles.contains(adminRole)){
            httpServletResponse.sendRedirect("/admin");
        } else if(roles.contains(userRole)){
            httpServletResponse.sendRedirect("/user");
        }
    }
}