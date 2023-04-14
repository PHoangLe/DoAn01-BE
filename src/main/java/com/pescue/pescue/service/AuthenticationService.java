package com.pescue.pescue.service;

import com.pescue.pescue.dto.*;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

        if (userService.findUserByUserEmail(request.getUserEmail()) != null){
            logger.error("Register user failed: " + request.getUserEmail() + "EXISTED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Tài khoản đã tồn tại")
                    .build());
        }

        try{
            userService.addUser(user);
        }
        catch (Exception e){
            logger.error("Register user failed: " + request.getUserEmail() + "EXISTED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi thêm thông tin người dùng vào database")
                    .build());
        }
        logger.trace("Successfully register user: " + request.getUserEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(StringResponseDTO.builder()
                .message("Tạo tài khoản mới thành công")
                .build());
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Bạn vui lòng xác thực tài khoản để đăng nhập")
                        .build());
            }

            if (user.isDeleted()) {
                logger.error("Authenticate user failed: " + request.getUserEmail() + " DELETED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Tài khoản này không còn tồn tại")
                        .build());
            }

            var jwtToken = jwtService.generateJwtToken(user);
            logger.trace("Successfully authenticate user: " + request.getUserEmail());
            return ResponseEntity.ok(AuthenticationResponseDTO.builder()
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( StringResponseDTO.builder()
                    .message("Tài khoản hoăc mật khẩu không hợp lệ")
                    .build());
        }
    }

    public ResponseEntity<Object> googleUserAuthenticate(GoogleUserAuthenticationRequestDTO request){
        User user = userService.findUserByUserEmail(request.getUserEmail());

        if (user == null){
            user = new User(request);
            if (!userService.addUser(user))
                logger.error("Authenticate user failed: " + request.getUserEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Có lỗi xảy ra khi thêm thông tin người dùng")
                        .build());
        }

        var jwtToken = jwtService.generateJwtToken(user);
        logger.trace("Successfully authenticate user: " + request.getUserEmail());
        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .jwtToken(jwtToken)
                .userID(user.getUserID())
                .userEmail(user.getUserEmail())
                .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                .userRoles(user.getUserRoles())
                .userAvatar(user.getUserAvatar())
                .build());
    }
}
