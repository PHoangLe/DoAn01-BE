package com.pescue.pescue.dto;

import com.pescue.pescue.model.Gender;
import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private String dob;
    private Gender userGender;
    private String userAvatar;
}
