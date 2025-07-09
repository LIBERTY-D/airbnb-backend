package com.daniel.app.airbnb.backend.service;

import com.daniel.app.airbnb.backend.clients.GitHubClient;
import com.daniel.app.airbnb.backend.clients.GitHubEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final GitHubClient githubClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //provider being issued whether GitHub, google, facebook etc...
        if ("github".equalsIgnoreCase(registrationId)) {
            String token = userRequest.getAccessToken().getTokenValue();
            List<GitHubEmail> emails = githubClient.getUserEmails("Bearer " + token);
            String primaryEmail = emails.stream()
                    .filter(e -> e.primary() && e.verified())
                    .map(GitHubEmail::email)
                    .findFirst()
                    .orElse(null);
            if (primaryEmail != null) {
                Map<String, Object> attributes = new HashMap<>(user.getAttributes());
                attributes.put("email", primaryEmail); // inject email
                return new DefaultOAuth2User(user.getAuthorities(), attributes, "id");
            }
        }

        return user;
    }
}
