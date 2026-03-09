package com.pg.booking.controller;

import com.pg.booking.dto.BookingResponseDto;
import com.pg.booking.dto.CreateBookingDto;
import com.pg.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody CreateBookingDto dto) {

        return ResponseEntity.ok(service.createBooking(dto));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.cancelBooking(id));
    }

    @PutMapping("/leave/{id}")
    public ResponseEntity<BookingResponseDto> leaveRoom(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.markTenantLeft(id));
    }

    @GetMapping("/tenant")
    public ResponseEntity<List<BookingResponseDto>> getTenantBookings() {
        return ResponseEntity.ok(service.getBookingsByTenant());
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings() {
        return ResponseEntity.ok(service.getBookingsByOwner());
    }
}
