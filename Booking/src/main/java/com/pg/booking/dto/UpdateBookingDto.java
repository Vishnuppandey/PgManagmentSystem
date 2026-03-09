package com.pg.booking.dto;

import com.pg.booking.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingDto {

    private BookingStatus status;
}
