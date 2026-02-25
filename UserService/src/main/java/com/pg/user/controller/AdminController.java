package com.pg.user.controller;

import com.pg.user.dto.CreateDto;
import com.pg.user.dto.ResponseDto;
import com.pg.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private UserService userService;
    public AdminController(UserService userService){
        this.userService=userService;
    }


    @PostMapping("/create-owner")
    public ResponseEntity<ResponseDto> createOwner(@RequestBody CreateDto dto) {
        return ResponseEntity.ok(userService.createOwner(dto));
    }
}
