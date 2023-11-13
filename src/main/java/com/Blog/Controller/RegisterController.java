package com.Blog.Controller;

import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;
import com.Blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegisterController {
    private final UserService service;
    @Autowired
    public RegisterController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDTO signupDTO){
        User existingEmail = service.findUserByEmail(signupDTO.getEmail());

        if(existingEmail != null){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword());

        service.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
