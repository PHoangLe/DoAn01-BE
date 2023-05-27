package com.pescue.pescue.dto;

import com.pescue.pescue.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String phoneNo;
    private Date dob;
    private Gender userGender;
    private String userAvatar;
}
