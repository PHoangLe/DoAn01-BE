package com.pescue.pescue.service;

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

    public User findUserByUserEmail(String emailAddress){
        return userRepository.findUserByUserEmail(emailAddress).get();
    }

    public boolean unlockUser(String emailAddress){
        Optional<User> user = userRepository.findUserByUserEmail(emailAddress);

        if (user.isEmpty())
            return false;

        user.get().setLocked(false);
        userRepository.save(user.get());
        return true;
    }
}
