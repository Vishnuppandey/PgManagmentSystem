package com.pg.dto;

import com.pg.enums.RoomStatus;
import com.pg.enums.SharingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponseDto {

    private Long id;
    private Integer roomNumber;
    private Integer floor;
    private SharingType sharingType;
    private Integer capacity;
    private Integer availableBeds;
    private Double rent;
    private RoomStatus status;
}
