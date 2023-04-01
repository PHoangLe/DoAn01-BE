package com.pescue.pescue.auth;

import com.pescue.pescue.dto.AuthenticationDTO;
import com.pescue.pescue.dto.UserRegisterDTO;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.service.JwtService;
import com.pescue.pescue.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public ResponseEntity<Object> userRegister(UserRegisterDTO request){
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
            logger.error("Register user failed: " + request.getUserEmail() + "EXISTED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản đã tồn tại");
        }
        logger.trace("Successfully register user: " + request.getUserEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo tài khoản mới thành công");
    }

    public ResponseEntity<Object> authenticate(AuthenticationDTO request){
        try{
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            User user = userService.findUserByUserEmail(request.getUserEmail());

            if (user.isLocked()) {
                logger.error("Authenticate user failed: " + request.getUserEmail() + " LOCKED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn vui lòng xác thực tài khoản để đăng nhập");
            }

            if (user.isDeleted()) {
                logger.error("Authenticate user failed: " + request.getUserEmail() + " DELETED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản này không còn tồn tại");
            }

            var jwtToken = jwtService.generateJwtToken(user);
            logger.trace("Successfully authenticate user: " + request.getUserEmail());
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .userID(user.getUserID())
                    .userEmail(user.getUserEmail())
                    .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                    .userRoles(user.getUserRoles())
                    .userAvatar(user.getUserAvatar())
                    .build());
        }
        catch (AuthenticationException e){
            logger.error("Authenticate user failed: " + request.getUserEmail() + " BAD_CREDENTIAL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản hoăc mật khẩu không hợp lệ");
        }
    }

    public ResponseEntity<Object> googleUserAuthenticate(GoogleUserAuthenticationRequest request){
        User user = userService.findUserByUserEmail(request.userEmail);

        if (user == null){
            user = new User(request.userEmail, request.userFirstName, request.userLastName, request.userAvatar, List.of(Role.ROLE_USER));
            if (!userService.addUser(user))
                logger.error("Authenticate user failed: " + request.getUserEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm thông tin người dùng");
        }

        var jwtToken = jwtService.generateJwtToken(user);
        logger.trace("Successfully authenticate user: " + request.getUserEmail());
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .userID(user.getUserID())
                .userEmail(user.getUserEmail())
                .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                .userRoles(user.getUserRoles())
                .userAvatar(user.getUserAvatar())
                .build());
    }
}
