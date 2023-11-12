package com.Blog.Config;

import com.Blog.Model.Privilege;
import com.Blog.Model.Role;
import com.Blog.Model.User;
import com.Blog.Repository.PrivilegeRepository;
import com.Blog.Repository.RoleRepository;
import com.Blog.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PrivilegeRepository privilegeRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup)return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivilege = Arrays.asList(readPrivilege,writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN",adminPrivilege);
        createRoleIfNotFound("ROLE_USER",Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setUsername("phucnguyen");
        user.setEmail("phucnguyen@gmail.com");
        user.setPassword(passwordEncoder.encode("123123"));
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name){
        Privilege privilege = privilegeRepository.findByName(name);
        if(privilege == null){
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege>privileges){
        Role role = roleRepository.findByName(name);
        if(role == null){
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
