package com.pg.booking.entity;

import com.pg.booking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="booking")
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;
    private Long tenantId;
    private Long ownerId;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    // ACTIVE, CANCELLED, LEFT

    private Boolean active;
}
