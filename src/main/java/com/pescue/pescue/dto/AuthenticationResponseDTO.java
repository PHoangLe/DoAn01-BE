package com.pescue.pescue.dto;

import com.pescue.pescue.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String jwtToken;
    private String userID;
    private String userEmail;
    private List<Role> userRoles;
    private String userFullName;
    private String userAvatar;
}