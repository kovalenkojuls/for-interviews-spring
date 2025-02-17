package ru.kovalenkojuls.securityoauth2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import ru.kovalenkojuls.securityoauth2.requestHandler.GitHubLogoutHandler;
import ru.kovalenkojuls.securityoauth2.requestHandler.GitHubSimpleLogoutHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GitHubLogoutHandler gitHubLogoutHandler;
    private final GitHubSimpleLogoutHandler gitHubSimpleLogoutHandler;

    public SecurityConfig(GitHubLogoutHandler gitHubLogoutHandler, GitHubSimpleLogoutHandler gitHubSimpleLogoutHandler) {
        this.gitHubLogoutHandler = gitHubLogoutHandler;
        this.gitHubSimpleLogoutHandler = gitHubSimpleLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2AuthorizedClientService authorizedClientService) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/profile", false)
                )
// --------------- Логаут с отзывом токена РАБОТАЕТ
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/public")
//                        .addLogoutHandler(gitHubLogoutHandler)
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
//                );

// -------------- Логаут на стороне гитхаба РАБОТАЕТ
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("https://github.com/logout")
//                        .addLogoutHandler(logoutHandler())
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
//                );

// -------------- Логаут средствами спринга НЕ РАБОТАЕТ
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/public")
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
//                        .addLogoutHandler(oauth2LogoutHandler(authorizedClientService))
//                        .addLogoutHandler(logoutHandler())
//                        .addLogoutHandler((request, response, authentication) -> {
//                            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//                            response.setHeader("Pragma", "no-cache");
//                            response.setHeader("Expires", "0");
//                        })
//                );

// -------------- Логаут средствами спринга НЕ РАБОТАЕТ
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(gitHubSimpleLogoutHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

//    @Bean
//    public LogoutHandler logoutHandler() {
//        return new SecurityContextLogoutHandler();
//    }

//    @Bean
//    public LogoutHandler oauth2LogoutHandler(OAuth2AuthorizedClientService authorizedClientService) {
//        return (request, response, authentication) -> {
//            System.out.println("oauth2LogoutHandler called");
//            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
//                String registrationId = oauthToken.getAuthorizedClientRegistrationId();
//                String principalName = oauthToken.getName();
//
//                System.out.println("Removing authorized client for " + registrationId + ", " + principalName);
//                authorizedClientService.removeAuthorizedClient(registrationId, principalName);
//
//                System.out.println("Clearing SecurityContextHolder");
//                SecurityContextHolder.clearContext();
//            }
//            request.getSession().invalidate();
//        };
//    }
}