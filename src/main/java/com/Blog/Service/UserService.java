package com.Blog.Service;

import com.Blog.DTO.SignupDTO;
import com.Blog.Model.User;
import com.Blog.Repository.RoleRepository;
import com.Blog.Repository.UserRepository;
import com.Blog.Service.Interface.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(SignupDTO signupDTO) {
        User user = modelMapper.map(signupDTO,User.class);
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setEnabled(false);
        return repository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }
}
