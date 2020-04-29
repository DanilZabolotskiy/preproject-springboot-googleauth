package ru.javamentor.preproject_springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.javamentor.preproject_springboot.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Set<String> roleNames = Util.extractRoleNames(SecurityContextHolder
                .getContext().getAuthentication().getAuthorities());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        if (roleNames.contains("admin")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (roleNames.contains("user")) {
            httpServletResponse.sendRedirect("/user");
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }

}