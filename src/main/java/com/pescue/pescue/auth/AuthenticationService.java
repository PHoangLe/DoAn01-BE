package com.pescue.pescue.auth;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import com.pescue.pescue.service.JwtService;
import com.pescue.pescue.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ResponseEntity<AuthenticationResponse> userRegister(UserRegisterRequest request){
        User user = new User(
                request.getUserEmail(),
                passwordEncoder.encode(request.getUserPassword()),
                request.getUserFirstName(),
                request.getUserLastName(),
                request.getUserAvatar(),
                true,
                List.of(Role.ROLE_USER)
        );
        try{
            userService.addUser(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder()
                    .errorMessage("Tài khoản email đã tồn tại")
                    .build());
        }
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .build());
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request){
        try{
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            User user = userService.findUserByUserEmail(request.getUserEmail());

            if (user.isLocked())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder()
                        .errorMessage("Bạn vui lòng xác thực tài khoản để đăng nhập")
                        .build());

            var jwtToken = jwtService.generateJwtToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .user(user)
                    .build());
        }
        catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder()
                    .errorMessage("Tài khoản hoăc mật khẩu không hợp lệ")
                    .build());
        }
    }

    public ResponseEntity<GoogleUserAuthenticationResponse> googleUserAuthenticate(GoogleUserAuthenticationRequest request){
        User googleUser = userService.findUserByUserEmail(request.userEmail);

        if (googleUser == null){
            googleUser = new User(request.userEmail, request.userFirstName, request.userLastName, request.userAvatar, List.of(Role.ROLE_USER));
            userService.addUser(googleUser);
        }

        var jwtToken = jwtService.generateJwtToken(googleUser);
        return ResponseEntity.ok(GoogleUserAuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .user(googleUser)
                .build());
    }
}
