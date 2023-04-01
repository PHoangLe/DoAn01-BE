package com.pescue.pescue.dto;

import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserAuthenticationResponseDTO {
    private String jwtToken;
    private String errorMessage;
    private User user;
}
