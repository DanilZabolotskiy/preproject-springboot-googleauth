package ru.javamentor.preproject_springboot.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Configuration()
public class GoogleOauthConfig {

    private final OAuth2ClientContext oAuth2ClientContext;

    public GoogleOauthConfig(OAuth2ClientContext oAuth2ClientContext) {
        this.oAuth2ClientContext = oAuth2ClientContext;
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        return new OAuth2RestTemplate(googleCodeResourceDetails(), oAuth2ClientContext);
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public AuthorizationCodeResourceDetails googleCodeResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.resource")
    public ResourceServerProperties googleServerProperties() {
        return new ResourceServerProperties();
    }

}
