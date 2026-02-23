package com.pg.user.dto;

import com.pg.user.enums.Role;
import com.pg.user.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Data
public class CreateDto {

    @NotNull
    private String name;
    private String email;
    private String phoneNo;
    private String password;
}
