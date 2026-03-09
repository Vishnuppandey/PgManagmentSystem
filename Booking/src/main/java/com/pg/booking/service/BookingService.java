package com.pg.booking.service;

import com.pg.booking.dto.BookingResponseDto;
import com.pg.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(CreateBookingDto dto);

    BookingResponseDto cancelBooking(Long bookingId);

    BookingResponseDto markTenantLeft(Long bookingId);

    List<BookingResponseDto> getBookingsByTenant();

    List<BookingResponseDto> getBookingsByOwner();
}
