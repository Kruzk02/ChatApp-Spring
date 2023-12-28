package com.Blog.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;
import com.Blog.Service.UserService;

@RestController
@RequestMapping("/api")
public class RegisterController {
    private final UserService service;
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDTO signupDTO) {

        logger.info("Registration attempt for username: {}", signupDTO.getUsername());

        User existingEmail = service.findUserByEmail(signupDTO.getEmail());

        if (existingEmail != null) {

            logger.warn("Registration failed. Email '{}' is already taken.", signupDTO.getEmail());
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User user = new User();
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword());

        service.saveUser(user);

        logger.info("User '{}' registered successfully.", signupDTO.getUsername());

        return ResponseEntity.ok("User registered successfully");
    }
}
