package com.pg.user.dto;

import com.pg.user.enums.Role;
import com.pg.user.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private String name;
    private String email;
    private String phoneNo;
    private String password;
    private Role role;
    private Status status;
}
