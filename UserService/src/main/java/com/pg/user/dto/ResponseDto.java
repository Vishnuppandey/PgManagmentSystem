package com.pg.user.dto;

import com.pg.user.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseDto {

    private String name;
    private String email;
    private String phoneNo;
    private Role role;
}
