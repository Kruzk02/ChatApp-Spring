package com.Blog.Controller;

import com.Blog.JWT.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/get-username")
    public ResponseEntity<String> getUsernameFromToken(@RequestHeader("Authorization") String authHeader){
        String token = extractToken(authHeader);

        if(token != null){
            String username = jwtService.extractUsername(token);
            logger.info("Username {} Extract successfully",username);
            return ResponseEntity.ok(username);
        }else{
            logger.warn("Extract username failed.");
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);

        if (token != null) {
            jwtService.addTokenToBlackList(token);
            logger.info("User Logout Successful. Token added to blacklist: {}", token);
            return ResponseEntity.ok("Logout Successful");
        } else {
            logger.warn("Invalid Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authHeader){
        String refreshToken = extractToken(authHeader);

        if(refreshToken != null){
            if(jwtService.isTokenBlackListed(refreshToken)){
                logger.warn("Refresh token is blacklisted: {}",refreshToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            String username = jwtService.extractUsername(refreshToken);
            String newToken = jwtService.generateToken(username);

            logger.info("Token refresh successful for user: {}",username);

            return ResponseEntity.ok(newToken);
        }else{
            logger.warn("Invalid Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }
    }
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
