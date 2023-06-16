package com.pescue.pescue.controller;

import com.pescue.pescue.dto.ChangePasswordDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.dto.UserProfileDTO;
import com.pescue.pescue.exception.InvalidPasswordException;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.User;
import com.pescue.pescue.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
@CrossOrigin
@Slf4j
@Api
public class UserController {

    private final UserService userService;

    @GetMapping("/getAllUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/getUserByEmail/{userEmail}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String userEmail) {
        User user = userService.findUserByUserEmail(userEmail);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại người dùng cần tìm")
                    .build());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getUserByID/{userID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getUserByUserID(@PathVariable String userID) {
        User user = userService.getUserByID(userID);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Không tồn tại người dùng cần tìm")
                    .build());
        return ResponseEntity.ok(user);
    }

   @PutMapping("/updateProfile")
   @PreAuthorize("hasAuthority('ROLE_USER')")
   @SecurityRequirement(name = "Bearer Authentication")
   public ResponseEntity<Object> updateProfile(@RequestBody UserProfileDTO userProfileDTO) throws Exception {
        User user = userService.updateUserProfile(userProfileDTO);
       return ResponseEntity.ok(user);
   }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO dto) throws InvalidPasswordException {
        userService.changePassword(dto);

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đổi mật khẩu thành công")
                .build());
    }
}
