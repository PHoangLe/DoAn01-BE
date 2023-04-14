package com.pescue.pescue.controller;

import com.pescue.pescue.service.AuthenticationService;
import com.pescue.pescue.dto.GoogleUserAuthenticationRequestDTO;
import com.pescue.pescue.dto.AuthenticationDTO;
import com.pescue.pescue.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/userRegister")
    public Object userRegister(@RequestBody UserRegisterDTO request){
        return authenticationService.userRegister(request);
    }

    @PostMapping("/authenticate")
    public Object authenticate(@RequestBody AuthenticationDTO request){
        return authenticationService.authenticate(request);
    }

    @PostMapping("/authenticateGoogleUser")
    public Object googleUserAuthenticate(@RequestBody GoogleUserAuthenticationRequestDTO request){

        return authenticationService.googleUserAuthenticate(request);
    }
}
