package com.pescue.pescue.auth;

import com.pescue.pescue.dto.OTPConfirmEmailDTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.service.EmailService;
import com.pescue.pescue.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "**")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final OTPService otpService;

    @PostMapping("/userRegister")
    public ResponseEntity<AuthenticationResponse> userRegister(@RequestBody UserRegisterRequest request){
        return authenticationService.userRegister(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    @GetMapping("/authenticateGoogleUser")
    public ResponseEntity<GoogleUserAuthenticationResponse> googleUserAuthenticate(@RequestBody GoogleUserAuthenticationRequest request){

        return authenticationService.googleUserAuthenticate(request);
    }

    @PostMapping("/sendOTPConfirmEmail")
    public ResponseEntity<String> sendMail(@RequestBody OTPConfirmEmailDTO request){
        return otpService.sendOTPConfirmEmail(request);
    }

    @PostMapping("/validateOTPConfirmEmail")
    public ResponseEntity<String> validateOTPConfirmEmail(@RequestBody ValidateOTPConfirmEmailDTO request){
        return otpService.validateOTPConfirmEmail(request);
    }
}
