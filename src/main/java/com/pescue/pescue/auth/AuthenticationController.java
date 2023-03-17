package com.pescue.pescue.auth;

import com.pescue.pescue.model.EmailDetails;
import com.pescue.pescue.requestbody.OTPConfirmEmailRequest;
import com.pescue.pescue.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "**")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/userRegister")
    public ResponseEntity<AuthenticationResponse> userRegister(@RequestBody UserRegisterRequest request){
        return ResponseEntity.ok(authenticationService.userRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/authenticateGoogleUser")
    public ResponseEntity<GoogleUserAuthenticationResponse> googleUserAuthenticate(@RequestBody GoogleUserAuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.googleUserAuthenticate(request));
    }

    @PostMapping("/sendOTPConfirmEmail")
    public String sendMail(@RequestBody OTPConfirmEmailRequest request){
        return emailService.sendOTPConfirmEmail(request);
    }
}
