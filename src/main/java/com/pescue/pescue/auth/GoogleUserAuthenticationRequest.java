package com.pescue.pescue.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserAuthenticationRequest {
    String userEmail;
    String userFirstName;
    String userLastName;
    String userAvatar;
}
