package ru.javamentor.preproject_springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import ru.javamentor.preproject_springboot.model.Role;
import ru.javamentor.preproject_springboot.model.User;
import ru.javamentor.preproject_springboot.service.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomUserInfoTokenService extends UserInfoTokenServices {

    private final UserService userService;
    private final OAuth2RestOperations restTemplate;
    private final AuthorizationCodeResourceDetails authorizationCodeResourceDetails;
    private final ResourceServerProperties resourceServerProperties;

    private String clientId;
    private String userInfoEndpointUrl;

    @Autowired
    public CustomUserInfoTokenService(UserService userService,
                                      OAuth2RestOperations restTemplate,
                                      AuthorizationCodeResourceDetails authorizationCodeResourceDetails,
                                      ResourceServerProperties resourceServerProperties) {
        super(resourceServerProperties.getUserInfoUri(), authorizationCodeResourceDetails.getClientId());
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.authorizationCodeResourceDetails = authorizationCodeResourceDetails;
        this.resourceServerProperties = resourceServerProperties;
        this.clientId = authorizationCodeResourceDetails.getClientId();
        this.userInfoEndpointUrl = resourceServerProperties.getUserInfoUri();
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        String googleId = (String) map.get("sub");
        Optional<User> userOptional = userService.getUserByGoogleId(googleId);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            String userName = (String) map.get("name");
            user = new User(userName,
                    userName,
                    googleId,
                    new HashSet<>(Collections.singletonList(new Role(2L, "user"))));
            userService.addUser(user);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, "", user.getAuthorities());
        OAuth2Request request = new OAuth2Request(null, accessToken, null, true, null,
                null, null, null, null);
        return new OAuth2Authentication(request, authentication);
    }

    private Map<String, Object> getMap(String path, String accessToken) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Getting user info from: " + path);
        }
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;
                token.setTokenType(tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            return restTemplate.getForEntity(path, Map.class).getBody();
        } catch (Exception ex) {
            this.logger.warn("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }

}
