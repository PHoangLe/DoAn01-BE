package com.pescue.pescue.controller;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.exception.InvalidException;
import com.pescue.pescue.exception.InvalidOtpException;
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

    @PostMapping("/sendOTPConfirmEmail")
    public ResponseEntity<Object> sendOTPConfirmEmail(@RequestBody OTP_DTO request){
        otpService.sendOTPConfirmEmail(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi mail thành công")
                .build());
    }

    @PostMapping("/validateOTPConfirmEmail")
    public ResponseEntity<Object> validateOTPConfirmEmail(@RequestBody ValidateOTPConfirmEmailDTO request) throws InvalidException {
        otpService.validateOTPConfirmEmail(request);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Mã OTP hợp lệ")
                .build());
    }
}
