package com.pescue.pescue.service;

import com.pescue.pescue.dto.ChangePasswordDTO;
import com.pescue.pescue.dto.UserProfileDTO;
import com.pescue.pescue.exception.InvalidPasswordException;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.constant.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){return userRepository.findAll();}

    public void updateUser(User user){
        userRepository.save(user);
    }

    public User findUserByUserEmail(String emailAddress){
        return userRepository.findUserByUserEmail(emailAddress).orElse(null);
    }

    public User getUserByID(String userID){
        log.trace("Finding user with ID: " + userID);
        return userRepository.findUserByUserID(userID).orElse(null);
    }

    public void unlockUser(String emailAddress){
        User user = userRepository.findUserByUserEmail(emailAddress).orElseThrow(UserNotFoundException::new);

        user.setLocked(false);
        userRepository.save(user);
    }

    public boolean addUser(User user) {
        try {
            userRepository.insert(user);
        }
        catch (Exception e){
            logger.error("There is an error occur while inserting user to database: " + user);
            return false;
        }
        logger.trace("User information has been added to database: " + user);
        return true;
    }

    public void addRoleForUser(String userID, Role role){
        User user = getUserByID(userID);
        List<Role> currentRole = user.getUserRoles();
        currentRole.add(role);
        user.setUserRoles(currentRole);

        updateUser(user);
    }

    public void changePassword(ChangePasswordDTO dto) throws InvalidPasswordException {
        User user = findUserByUserEmail(dto.getUserEmail());

        if (user == null)
            throw new UserNotFoundException();

        if (user.getUserPassword() != null && !BCrypt.checkpw(dto.getUserOldPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        user.setUserPassword(passwordEncoder.encode(dto.getUserNewPassword()));

        userRepository.save(user);
        logger.trace("Changed password of User: " + dto.getUserEmail());
    }

    public User updateUserProfile(UserProfileDTO userProfileDTO) throws Exception {
        User user = getUserByID(userProfileDTO.getUserID());

        if (user == null) {
            log.error("Can't find any user with Id: " + userProfileDTO.getUserID());
            throw new UserNotFoundException();
        }

        user.setUserFirstName(userProfileDTO.getUserFirstName());
        user.setUserLastName(userProfileDTO.getUserLastName());
        user.setPhoneNo(userProfileDTO.getPhoneNo());
        user.setDob(stringToDate(userProfileDTO.getDob()));
        user.setUserGender(userProfileDTO.getUserGender());
        user.setUserAvatar(userProfileDTO.getUserAvatar());

        updateUser(user);
        logger.trace("Updated user with Id: " + user.getUserID());
        return user;
    }


    private Date stringToDate(String dob) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(dob);
    }

    public void resetPassword(String userEmail, String newPassword) {
        User user = findUserByUserEmail(userEmail);

        user.setUserPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        logger.trace("Reset password of User: " + userEmail);
    }
}
