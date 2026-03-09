package com.pg.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingDto {

    private Long roomId;

    // ownerId is optional depending on your flow
    // but usually needed so owner can track tenants
    private String pgName;
}
