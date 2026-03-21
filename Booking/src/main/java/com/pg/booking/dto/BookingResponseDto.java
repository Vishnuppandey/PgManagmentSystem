package com.pg.booking.dto;

import com.pg.booking.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingResponseDto {

    private Long id;

    private Long roomId;

    private Long tenantId;

    private Long ownerId;

    private LocalDate joinDate;

    private BookingStatus status;
    
    private Integer seatsLeft;
}
