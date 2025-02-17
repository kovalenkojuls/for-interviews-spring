package ru.kovalenkojuls.securityoauth2.requestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Slf4j
@Component
public class GitHubLogoutHandler implements LogoutHandler {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate;

    public GitHubLogoutHandler(ClientRegistrationRepository clientRegistrationRepository,
                               OAuth2AuthorizedClientService authorizedClientService,
                               RestTemplate restTemplate) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("GitHub Logout Handler called");

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("github");
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistration.getRegistrationId(),
                    oauthToken.getName()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-GitHub-Api-Version", "2022-11-28");
            String auth = clientRegistration.getClientId() + ":" + clientRegistration.getClientSecret();
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);

            String requestBody = String.format("{\"access_token\":\"%s\"}", authorizedClient.getAccessToken().getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> res = restTemplate.exchange(
                        String.format("https://api.github.com/applications/%s/grant", clientRegistration.getClientId()),
                        HttpMethod.DELETE,
                        entity,
                        String.class);
                if (res.getStatusCode().is2xxSuccessful()) {
                    log.info("GitHub token revoked successfully");
                    authorizedClientService.removeAuthorizedClient(
                            clientRegistration.getRegistrationId(),
                            oauthToken.getName());
                } else {
                    log.error("Error revoking GitHub token: {}", res.getBody());
                }
            } catch (Exception e) {
                log.error("Error revoking GitHub token", e);
            }
        } else {
            log.warn("Authentication is not an OAuth2AuthenticationToken: {}", authentication.getClass());
        }
    }
}