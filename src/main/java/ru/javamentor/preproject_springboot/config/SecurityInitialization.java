package ru.javamentor.preproject_springboot.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(2)
public class SecurityInitialization extends AbstractSecurityWebApplicationInitializer {
}
