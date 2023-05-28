package com.pescue.pescue.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pescue.pescue.dto.GoogleUserAuthenticationRequestDTO;
import com.pescue.pescue.dto.UserRegisterDTO;
import com.pescue.pescue.exception.DateOfBirthFormatException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Document("User")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    private String userID;
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String phoneNo;
    private Date dob;
    private Gender userGender;
    private String userAvatar;
    private boolean isLocked = true;
    private boolean isDeleted = false;
    private List<Role> userRoles;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @JsonIgnore
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    @JsonIgnore
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public User(String userEmail, String userPassword, String userFirstName, String userLastName, String phoneNo, Date dob, Gender userGender, String userAvatar, boolean isLocked, List<Role> userRoles) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.userGender = userGender;
        this.userAvatar = userAvatar;
        this.isLocked = isLocked;
        this.userRoles = userRoles;
    }

    public User(GoogleUserAuthenticationRequestDTO dto){
        this.userEmail = dto.getUserEmail();
        this.userFirstName = dto.getUserFirstName();
        this.userLastName = dto.getUserLastName();
        this.phoneNo = "none";
        this.dob = new Date(System.currentTimeMillis());
        this.userGender = Gender.OTHER;
        this.userAvatar = dto.getUserAvatar();
        this.isLocked = false;
        this.isDeleted = false;
        this.userRoles = List.of(Role.ROLE_USER);
    }
    public User(UserRegisterDTO dto) {
        this.userEmail = dto.getUserEmail();
        this.userFirstName = dto.getUserFirstName();
        this.userLastName = dto.getUserLastName();
        this.phoneNo = dto.getPhoneNo();
        this.userGender = dto.getUserGender();
        this.userAvatar = dto.getUserAvatar();
        this.isLocked = false;
        this.isDeleted = false;
        this.userRoles = List.of(Role.ROLE_USER);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        userRoles.forEach(_role -> {
            roles.add(new SimpleGrantedAuthority(_role.name()));
        });
        return roles;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return userPassword;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return userEmail;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
