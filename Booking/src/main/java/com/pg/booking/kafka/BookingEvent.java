package com.pg.booking.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {

    private Long bookingId;
    private Long roomId;
    private Long tenantId;
    private String eventType;
}
