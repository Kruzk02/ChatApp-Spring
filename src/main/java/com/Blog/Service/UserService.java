package com.Blog.Service;

import com.Blog.Model.User;
import com.Blog.Repository.RoleRepository;
import com.Blog.Repository.UserRepository;
import com.Blog.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User saveUser(User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        return repository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }
}
