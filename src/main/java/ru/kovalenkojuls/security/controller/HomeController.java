package ru.kovalenkojuls.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Public page");
    }

    @GetMapping("/protected")
    public ResponseEntity<String> protectedPage() {
        return ResponseEntity.ok("Protected Page");
    }
}
