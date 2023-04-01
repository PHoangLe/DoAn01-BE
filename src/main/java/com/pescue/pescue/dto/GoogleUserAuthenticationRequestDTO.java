package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserAuthenticationRequestDTO {
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userAvatar;
}
