package com.pescue.pescue.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpForgotPasswordDTO extends ValidateOtpConfirmEmailDTO {
    private String newPassword;
}
