package com.pg.dto;

import com.pg.enums.RoomStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RoomInternalDto {

	private Long id;

    private Long ownerId;

    private Integer availableBeds;

    private RoomStatus status;
}
