package com.pg.booking.kafka;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingEvent {

    private Long bookingId;
    private Long roomId;
    private Long tenantId;
    private String eventType;
}
