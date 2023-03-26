package com.pescue.pescue.auth;

import com.pescue.pescue.dto.AuthenticationDTO;
import com.pescue.pescue.dto.UserRegisterDTO;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Object userRegister(UserRegisterDTO request){
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticationResponse.builder()
                .build());
    }

    public Object authenticate(AuthenticationDTO request){
        try{
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            User user = userService.findUserByUserEmail(request.getUserEmail());

            if (user.isLocked())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn vui lòng xác thực tài khoản để đăng nhập");

            if (user.isDeleted())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản này không còn tồn tại");

            var jwtToken = jwtService.generateJwtToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .userID(user.getUserID())
                    .userEmail(user.getUserEmail())
                    .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                    .userRoles(user.getUserRoles())
                    .build());
        }
        catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản hoăc mật khẩu không hợp lệ");
        }
    }

    public Object googleUserAuthenticate(GoogleUserAuthenticationRequest request){
        User user = userService.findUserByUserEmail(request.userEmail);

        if (user == null){
            user = new User(request.userEmail, request.userFirstName, request.userLastName, request.userAvatar, List.of(Role.ROLE_USER));
            userService.addUser(user);
        }

        var jwtToken = jwtService.generateJwtToken(user);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .userID(user.getUserID())
                .userEmail(user.getUserEmail())
                .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                .userRoles(user.getUserRoles())
                .build());
    }
}
