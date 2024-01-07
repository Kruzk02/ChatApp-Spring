package com.Blog.Controller;

import com.Blog.Model.VerificationToken;
import com.Blog.Service.EmailService;
import com.Blog.Service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;
import com.Blog.Service.UserService;

@RestController
@RequestMapping("/api")
public class RegisterController {
    private final UserService service;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService service, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.service = service;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
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
        user.setEnabled(false);

        User savedUser =  service.saveUser(user);
        VerificationToken verificationToken = verificationTokenService.generateVerificationToken(savedUser);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());

        logger.info("User '{}' registered successfully.Check your email for verification.", signupDTO.getUsername());

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        try {
            verificationTokenService.verifyAccount(token);
            return ResponseEntity.ok("Account verified successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token verification failed or expired");
        }
    }
}
