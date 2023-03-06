package com.pescue.pescue.controller;

import com.pescue.pescue.model.User;
import com.pescue.pescue.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/getAllUser")
    public List<User> fetchAllUser() {
        return userService.getAllUser();
    }

}
