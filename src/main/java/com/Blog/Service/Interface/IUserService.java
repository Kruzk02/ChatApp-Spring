package com.Blog.Service.Interface;

import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;

public interface IUserService {
    User saveUser(SignupDTO signupDTO);
    void update(User user);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
