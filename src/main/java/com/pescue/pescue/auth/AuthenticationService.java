package com.pescue.pescue.auth;

import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import com.pescue.pescue.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse userRegister(UserRegisterRequest request){
        User user = new User(
                request.getUserEmail(),
                passwordEncoder.encode(request.getUserPassword()),
                request.getUserFirstName(),
                request.getUserLastName(),
                request.getUserAvatar(),
                List.of(Role.ROLE_USER)
        );
        try{
            userRepository.insert(user);
        }
        catch (Exception e){
            return AuthenticationResponse.builder()
                    .exception(e)
                    .build();
        }
        return AuthenticationResponse.builder()
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try{
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            User user = userRepository.findUserByUserEmail(request.getUserEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateJwtToken(user);
            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .build();
        }
        catch (AuthenticationException e){
            return AuthenticationResponse.builder()
                    .authException(e)
                    .build();
        }
    }

}
