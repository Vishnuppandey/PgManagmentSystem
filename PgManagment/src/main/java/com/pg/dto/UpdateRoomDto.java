package com.pg.dto;

import com.pg.enums.RoomStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoomDto {

    private Double rent;
    private RoomStatus status;
    private Boolean active;
}
