package com.pescue.pescue.service;

import com.pescue.pescue.model.GoogleUser;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUser(){return userRepository.findAll();}

    public User saveUser(User user){
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(Role.ROLE_USER);
        user.setUserRoles(userRoles);
        userRepository.insert(user);
        return user;
    }
}
