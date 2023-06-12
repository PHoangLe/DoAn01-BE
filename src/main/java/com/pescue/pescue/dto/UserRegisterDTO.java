package com.pescue.pescue.dto;

import com.pescue.pescue.model.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Gender userGender;
    private String userAvatar;
}
