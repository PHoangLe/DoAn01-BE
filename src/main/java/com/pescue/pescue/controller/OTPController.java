package com.pescue.pescue.controller;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.dto.ValidateOtpConfirmEmailDTO;
import com.pescue.pescue.dto.ValidateOtpForgotPasswordDTO;
import com.pescue.pescue.exception.InvalidException;
import com.pescue.pescue.service.OTPService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/otp")
@CrossOrigin
@Api
public class OTPController {
    private final OTPService otpService;
    //Confirm Email
    @PostMapping("/sendOTPConfirmEmail")
    public ResponseEntity<Object> sendOTPConfirmEmail(@RequestBody OTP_DTO request){
        otpService.sendOTPConfirmEmail(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi mail thành công")
                .build());
    }

    @PostMapping("/validateOTPConfirmEmail")
    public ResponseEntity<Object> validateOTPConfirmEmail(@RequestBody ValidateOtpConfirmEmailDTO request) throws InvalidException {
        otpService.validateOTPConfirmEmail(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Mã OTP hợp lệ")
                .build());
    }

    //Forgot Password
    @PostMapping("/sendOTPForgotPassword")
    public ResponseEntity<Object> sendOTPForgotPassword(@RequestBody OTP_DTO request){
        otpService.sendOTPForgotPassowrd(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi mail thành công")
                .build());
    }

    @PostMapping("/validateOTPForgotPassword")
    public ResponseEntity<Object> validateOTPForgotPassword(@RequestBody ValidateOtpForgotPasswordDTO request) throws InvalidException {
        otpService.validateOTPForgotPassword(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Mã OTP hợp lệ")
                .build());
    }
}
