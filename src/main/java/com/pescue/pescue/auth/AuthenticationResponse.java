package com.pescue.pescue.auth;

import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String jwtToken;
    private String userID;
    private String userEmail;
    private List<Role> userRoles;
    private String userFullName;
}
