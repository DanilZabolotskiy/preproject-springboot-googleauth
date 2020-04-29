package ru.javamentor.preproject_springboot.filter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.javamentor.preproject_springboot.util.Util;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class GoogleOauthRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Set<String> roleNames = Util.extractRoleNames(SecurityContextHolder
                .getContext().getAuthentication().getAuthorities());
        response.setStatus(HttpServletResponse.SC_OK);
        if (roleNames.contains("admin")) {
            response.sendRedirect("/admin");
        } else if (roleNames.contains("user")) {
            response.sendRedirect("/user");
        } else {
            response.sendRedirect("/login");
        }
    }

}