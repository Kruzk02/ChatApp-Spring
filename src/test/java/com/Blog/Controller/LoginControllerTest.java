package com.Blog.Controller;

import com.Blog.DTO.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authUseSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("test@example.com");
        loginDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        ResponseEntity<String> responseEntity = loginController.authUser(loginDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User signed-in successfully!.", responseEntity.getBody());
    }

    @Test
    void authUserInvalidCredentials() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("test@example.com");
        loginDTO.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<String> responseEntity = loginController.authUser(loginDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid email or password.", responseEntity.getBody());
    }

    @Test
    void authUserEmptyFields() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("");

        ResponseEntity<String> responseEntity = loginController.authUser(loginDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email and password cannot be empty.", responseEntity.getBody());
    }
}
