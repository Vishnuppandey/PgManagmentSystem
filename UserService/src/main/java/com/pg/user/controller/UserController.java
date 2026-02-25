package com.pg.user.controller;

import com.pg.user.dto.CreateDto;
import com.pg.user.dto.LoginRequest;
import com.pg.user.dto.ResponseDto;
import com.pg.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
