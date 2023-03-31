package com.pescue.pescue.service;

import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUser(){return userRepository.findAll();}

    public boolean updateUser(User user){
        try {
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public User findUserByUserEmail(String emailAddress){
        if(userRepository.findUserByUserEmail(emailAddress).isPresent())
            return userRepository.findUserByUserEmail(emailAddress).get();
        return null;
    }

    public User findUserByID(String userID){
        return userRepository.findUserByUserID(userID).get();
    }

    public boolean unlockUser(String emailAddress){
        Optional<User> user = userRepository.findUserByUserEmail(emailAddress);

        if (user.isEmpty())
            return false;

        user.get().setLocked(false);
        userRepository.save(user.get());
        return true;
    }

    public boolean addUser(User user) {
        try {
            userRepository.insert(user);
        }
        catch (Exception e){
//            logger.error("There is an error occur while adding user to database: " + user);
            return false;
        }
//        logger.trace("User information has been added to database: " + user);
        return true;
    }

    public boolean addRoleForUser(String userID, Role role){
        User user = findUserByID(userID);
        List<Role> currentRole = user.getUserRoles();
        currentRole.add(role);
        user.setUserRoles(currentRole);

        if(!updateUser(user)) {
//            logger.error("There is an error occur while adding role for user: " + userID);
            return false;
        }
        return true;
    }
}
