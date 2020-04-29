package ru.javamentor.preproject_springboot.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.javamentor.preproject_springboot.filter.GoogleOauthRedirectFilter;
import ru.javamentor.preproject_springboot.service.UserService;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan(basePackages = {
        "ru.javamentor.preproject_springboot.service",
        "ru.javamentor.preproject_springboot.repository",
        "ru.javamentor.preproject_springboot.config"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2RestTemplate oAuth2RestTemplate;
    private final CustomUserInfoTokenService tokenService;
    private final GoogleOauthRedirectFilter googleOauthRedirectFilter;

    public SecurityConfig(UserAuthenticationSuccessHandler authenticationSuccessHandler, OAuth2RestTemplate oAuth2RestTemplate, CustomUserInfoTokenService tokenService, UserService userService, GoogleOauthRedirectFilter googleOauthRedirectFilter) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.tokenService = tokenService;
        this.googleOauthRedirectFilter = googleOauthRedirectFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("admin")
                .antMatchers("/user").hasAuthority("user")
                .antMatchers("/get_auth_user").hasAnyAuthority("user", "admin")
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_process")
                .failureUrl("/login?error")
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll()
                .successHandler(authenticationSuccessHandler);

        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);

        http.addFilterBefore(googleOauthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private Filter googleOauthFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        googleFilter.setRestTemplate(oAuth2RestTemplate);
        googleFilter.setTokenServices(tokenService);
        return googleFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public FilterRegistrationBean<GoogleOauthRedirectFilter> someFilterRegistration() {
        FilterRegistrationBean<GoogleOauthRedirectFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(googleOauthRedirectFilter);
        registration.addUrlPatterns("/");
        return registration;
    }

}
