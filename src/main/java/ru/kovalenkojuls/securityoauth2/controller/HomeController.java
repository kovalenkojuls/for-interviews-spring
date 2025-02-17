package ru.kovalenkojuls.securityoauth2.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/public")
    public String publicPage() {
        return "public";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User principal, Model model, HttpServletResponse response) {

        String login = principal.getAttribute("login");
        log.info("Successful authentication of user: {}", login);

        model.addAttribute("id", principal.getAttribute("id"));
        model.addAttribute("login", principal.getAttribute("login"));
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));

        return "profile";
    }
}
