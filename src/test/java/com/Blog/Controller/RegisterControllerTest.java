package com.Blog.Controller;

import com.Blog.Controller.RegisterController;
import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;
import com.Blog.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUsername("test");
        signupDTO.setEmail("test@example.com");
        signupDTO.setPassword("password");

        when(userService.findUserByEmail("test@example.com")).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = registerController.registerUser(signupDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User registered successfully", responseEntity.getBody());
    }

    @Test
    void registerUser_Failure_EmailTaken() {
        // Arrange
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUsername("testuser");
        signupDTO.setEmail("test@example.com");
        signupDTO.setPassword("password");

        when(userService.findUserByEmail("test@example.com")).thenReturn(new User());

        // Act
        ResponseEntity<?> responseEntity = registerController.registerUser(signupDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email is already taken!", responseEntity.getBody());
    }
}
