package ru.kovalenkojuls.securityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kovalenkojuls.securityjwt.dto.AuthRequest;
import ru.kovalenkojuls.securityjwt.filter.JwtRequestFilter;

@RestController
public class HomeController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public HomeController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Public page");
    }

    @GetMapping("/protected")
    public ResponseEntity<String> protectedPage() {
        return ResponseEntity.ok("Protected Page");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
        final String jwt = jwtRequestFilter.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }
}
