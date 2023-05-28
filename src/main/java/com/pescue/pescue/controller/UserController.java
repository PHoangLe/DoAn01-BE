package com.pescue.pescue.controller;

import com.pescue.pescue.dto.ChangePasswordDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.dto.UserProfileDTO;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.User;
import com.pescue.pescue.service.UserService;
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
        if (userService.findUserByID(userID) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Không tồn tại người dùng cần tìm")
                    .build());
        return ResponseEntity.ok(userService.findUserByID(userID));
    }

   @PutMapping("/updateProfile")
   @PreAuthorize("hasAuthority('ROLE_USER')")
   @SecurityRequirement(name = "Bearer Authentication")
   public ResponseEntity<Object> updateProfile(@RequestBody UserProfileDTO userProfileDTO){
        User user;
        try{
            user = userService.updateUserProfile(userProfileDTO);
        }
        catch (UserNotFoundException unfe){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message(unfe.getMessage())
                    .build());
        }
        catch (ParseException pe){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Ngày sinh không đúng định dạng vui lòng kiểm tra lại")
                    .build());
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                    .message("Đã có lỗi xảy ra khi cập nhật thông tin của bạn. Vui lòng thử lại sau")
                    .build());
        }
       return ResponseEntity.ok(user);
   }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO dto){
        User user = userService.findUserByUserEmail(dto.getUserEmail());

        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Người dùng không tồn tại")
                    .build());

        if(user.getUserPassword() != null && !BCrypt.checkpw(dto.getUserOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Mật khẩu hiện tại không trùng khớp")
                    .build());
        }

        if (!userService.changePassword(dto, user))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                    .message("Có lỗi đã xảy ra khi đổi mật khẩu")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đổi mật khẩu thành công")
                .build());
    }
}
