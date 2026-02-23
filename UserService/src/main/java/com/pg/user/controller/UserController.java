package com.pg.user.controller;

import com.pg.user.dto.CreateDto;
import com.pg.user.dto.ResponseDto;
import com.pg.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateDto user){
        ResponseDto createdUser=userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
