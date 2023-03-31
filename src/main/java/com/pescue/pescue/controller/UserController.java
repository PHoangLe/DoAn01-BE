package com.pescue.pescue.controller;

import com.pescue.pescue.model.User;
import com.pescue.pescue.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
@CrossOrigin("**")
public class UserController {

    private final UserService userService;

    @GetMapping("/getAllUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> fetchAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

}
