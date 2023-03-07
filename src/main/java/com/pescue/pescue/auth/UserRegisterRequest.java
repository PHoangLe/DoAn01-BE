package com.pescue.pescue.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userAvatar;
}
