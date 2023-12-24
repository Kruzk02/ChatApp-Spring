package com.Blog.Controller;

import com.Blog.JWT.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JwtService jwtService;
    @Autowired
    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @GetMapping("/get-username")
    public ResponseEntity<String> getUsernameFromToken(@RequestHeader("Authorization") String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            return ResponseEntity.ok(username);
        }else{
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }
}
