package com.Blog.Service.Interface;

import com.Blog.Model.User;

public interface IUserService {
    User saveUser(User user);
    User findUserByEmail(String email);
}
