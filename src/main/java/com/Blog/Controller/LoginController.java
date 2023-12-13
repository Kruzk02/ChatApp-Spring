package com.Blog.Controller;

import com.Blog.DTO.LoginDTO;
import com.Blog.JWT.JwtService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authUser(@RequestBody LoginDTO loginDTO){
        if (StringUtils.isEmpty(loginDTO.getEmail()) || StringUtils.isEmpty(loginDTO.getPassword())) {
            return new ResponseEntity<>("Email and password cannot be empty.", HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(loginDTO.getEmail());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
        }
    }
}
