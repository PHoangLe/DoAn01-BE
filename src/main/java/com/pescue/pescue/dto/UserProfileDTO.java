package com.pescue.pescue.dto;

import com.pescue.pescue.model.Gender;
import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String userID;
    private String userFirstName;
    private String userLastName;
    private String phoneNo;
    private Date dob;
    private Gender userGender;
    private String userAvatar;

    public UserProfileDTO(User user){
        this.userID = user.getUserID();
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.phoneNo = user.getPhoneNo();
        this.dob = user.getDob();
        this.userGender = user.getUserGender();
        this.userAvatar = user.getUserAvatar();
    }
}
