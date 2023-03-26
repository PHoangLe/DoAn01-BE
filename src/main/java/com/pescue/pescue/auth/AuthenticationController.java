package com.pescue.pescue.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/userRegister")
    public Object userRegister(@RequestBody UserRegisterRequest request){
        return authenticationService.userRegister(request);
    }

    @PostMapping("/authenticate")
    public Object authenticate(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    @GetMapping("/authenticateGoogleUser")
    public Object googleUserAuthenticate(@RequestBody GoogleUserAuthenticationRequest request){

        return authenticationService.googleUserAuthenticate(request);
    }
}
