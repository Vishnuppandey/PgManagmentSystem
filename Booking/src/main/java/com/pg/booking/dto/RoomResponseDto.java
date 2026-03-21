package com.pg.booking.dto;

import com.pg.booking.enums.RoomStatus;
import com.pg.booking.enums.SharingType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponseDto {

	private Long id;

    private Long ownerId;

    private Integer availableBeds;

    private RoomStatus status;
}
