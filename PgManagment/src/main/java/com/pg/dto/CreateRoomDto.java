package com.pg.dto;

import com.pg.enums.SharingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomDto {

    private Integer roomNumber;
    private Integer floor;
    private SharingType sharingType;
    private Integer capacity;
    private Double rent;
}
