package com.pescue.pescue.auth;

import com.pescue.pescue.model.GoogleUser;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.GoogleUserRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GoogleUserRepository googleUserRepository;
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
                    .errorMessage("Tài khoản email đã tồn tại")
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
                    .user(user)
                    .build();
        }
        catch (AuthenticationException e){
            return AuthenticationResponse.builder()
                    .errorMessage("Tài khoản hoăc mật khẩu không hợp lệ")
                    .build();
        }
    }

    public GoogleUserAuthenticationResponse googleUserAuthenticate(GoogleUserAuthenticationRequest request){
        GoogleUser googleUser = googleUserRepository.findUserByUserEmail(request.userEmail);

        if (googleUser == null){
            GoogleUser googleUser1 = new GoogleUser(request.userEmail, request.userFirstName, request.userLastName, request.userAvatar, List.of(Role.ROLE_USER));
            googleUserRepository.insert(googleUser1);
            var jwtToken = jwtService.generateJwtToken(googleUser1);
            return GoogleUserAuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .user(googleUser1)
                    .build();
        }

        var jwtToken = jwtService.generateJwtToken(googleUser);
        return GoogleUserAuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .user(googleUser)
                .build();
    }
}
