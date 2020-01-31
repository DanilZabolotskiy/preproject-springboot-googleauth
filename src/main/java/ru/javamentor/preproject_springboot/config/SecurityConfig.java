package ru.javamentor.preproject_springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.javamentor.preproject_springboot.model.User;
import ru.javamentor.preproject_springboot.security.CustomUserInfoTokenService;
import ru.javamentor.preproject_springboot.security.UserAuthenticationSuccessHandler;
import ru.javamentor.preproject_springboot.service.UserDetailsServiceImpl;
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

    @Autowired
    UserAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    OAuth2ClientContext oAuth2ClientContext;
    @Autowired
    UserService userService;

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    .csrf().disable()
                .cors().disable();

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

        http.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserService userService) {
        return map -> {
            map.forEach((e,v)->{
                System.out.println(e + " ::: " + v);
            });
            User user = userService.getUserByLogin("1").get();
            System.out.println(user);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
            return user;
        };
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter(){
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenService tokenService = new CustomUserInfoTokenService(googleResource().getUserInfoUri(), google().getClientId());
        tokenService.setRestTemplate(googleTemplate);
        tokenService.setUserService(userService);
        tokenService.setPasswordEncoder(bCryptPasswordEncoder);
        googleFilter.setTokenServices(tokenService);
        return googleFilter;
    }



    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }

}
