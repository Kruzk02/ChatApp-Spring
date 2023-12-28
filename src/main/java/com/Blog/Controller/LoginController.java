package com.Blog.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.Blog.DTO.LoginDTO;
import com.Blog.JWT.JwtService;
import io.micrometer.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authUser(@RequestBody LoginDTO loginDTO) {
        try {

            logger.info("Login attempt for username: {}", loginDTO.getUsername());

            if (StringUtils.isEmpty(loginDTO.getUsername()) || StringUtils.isEmpty(loginDTO.getPassword())) {

                logger.warn("Invalid login request. Username or password is empty.");
                return ResponseEntity.badRequest().body("Username and password cannot be empty.");
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(loginDTO.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            logger.info("User '{}' logged in successfully.", loginDTO.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Login failed for username: {}", loginDTO.getUsername());

            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }
}
