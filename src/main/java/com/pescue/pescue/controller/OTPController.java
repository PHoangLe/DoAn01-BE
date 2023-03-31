package com.pescue.pescue.controller;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.service.OTPService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/otp")
@CrossOrigin("**")
public class OTPController {
    private final OTPService otpService;

    @PostMapping("/sendOTPConfirmEmail")
    public ResponseEntity<Object> sendOTPConfirmEmail(@RequestBody OTP_DTO request){
        return otpService.sendOTPConfirmEmail(request);
    }

    @PostMapping("/validateOTPConfirmEmail")
    public ResponseEntity<Object> validateOTPConfirmEmail(@RequestBody ValidateOTPConfirmEmailDTO request){
        return otpService.validateOTPConfirmEmail(request);
    }
}
