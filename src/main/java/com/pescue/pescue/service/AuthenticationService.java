package com.pescue.pescue.service;

import com.pescue.pescue.dto.*;
import com.pescue.pescue.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    public ResponseEntity<Object> userRegister(UserRegisterDTO request){
        User user = userService.findUserByUserEmail(request.getUserEmail());
        if (user != null){
            if (!user.isLocked()) {
                log.error("Register user failed: " + request.getUserEmail() + "EXISTED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Tài khoản đã tồn tại")
                        .build());
            }
            else {
                log.error("Register user failed: " + request.getUserEmail() + "NON_APPROVED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Tài khoản đã tồn tại nhưng chưa được xác thực. Bạn vui lòng xác thực tài khoản để đăng nhập")
                        .build());
            }
        }
        user = new User(request);
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        try{
            userService.addUser(user);
        }
        catch (Exception e){
            log.error("Register user failed: " + request.getUserEmail() + "EXISTED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi thêm thông tin người dùng vào database")
                    .build());
        }
        log.trace("Successfully register user: " + request.getUserEmail());
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
                log.error("Authenticate user failed: " + request.getUserEmail() + " LOCKED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Bạn vui lòng xác thực tài khoản để đăng nhập")
                        .build());
            }

            if (user.isDeleted()) {
                log.error("Authenticate user failed: " + request.getUserEmail() + " DELETED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Tài khoản này không còn tồn tại")
                        .build());
            }

            var jwtToken = jwtService.generateJwtToken(user);
            log.trace("Successfully authenticate user: " + request.getUserEmail());
            return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                    .jwtToken(jwtToken)
                    .userID(user.getUserID())
                    .userEmail(user.getUserEmail())
                    .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                    .phoneNo(user.getPhoneNo())
                    .userGender(user.getUserGender())
                    .dob(user.getDob())
                    .userRoles(user.getUserRoles())
                    .userAvatar(user.getUserAvatar())
                    .build());
        }
        catch (AuthenticationException e){
            log.error("Authenticate user failed: " + request.getUserEmail() + " BAD_CREDENTIAL");
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
                log.error("Authenticate user failed: " + request.getUserEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Có lỗi xảy ra khi thêm thông tin người dùng")
                        .build());
        }

        var jwtToken = jwtService.generateJwtToken(user);
        log.trace("Successfully authenticate user: " + request.getUserEmail());
        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .jwtToken(jwtToken)
                .userID(user.getUserID())
                .userEmail(user.getUserEmail())
                .userFullName(user.getUserFirstName() + " " + user.getUserLastName())
                .phoneNo(user.getPhoneNo())
                .userGender(user.getUserGender())
                .dob(user.getDob())
                .userRoles(user.getUserRoles())
                .userAvatar(user.getUserAvatar())
                .build());
    }
}
