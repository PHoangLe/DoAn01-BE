package com.pescue.pescue.auth;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.pescue.pescue.model.GoogleUser;
import com.pescue.pescue.model.Role;
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
public class GoogleUserAuthenticationResponse {
    private String jwtToken;
    private String errorMessage;
    private GoogleUser user;
}
