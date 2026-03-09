package com.pg.booking.service;

import com.pg.booking.dto.BookingResponseDto;
import com.pg.booking.dto.CreateBookingDto;
import com.pg.booking.entity.Booking;
import com.pg.booking.enums.BookingStatus;
import com.pg.booking.kafka.BookingEvent;
import com.pg.booking.kafka.BookingEventProducer;
import com.pg.booking.repository.BookingRepository;
import com.pg.booking.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingEventProducer producer;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public BookingServiceImpl(BookingRepository repository,
                              BookingEventProducer producer,
                              JwtUtil jwtUtil,
                              HttpServletRequest request) {
        this.repository = repository;
        this.producer = producer;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    @Override
    @PreAuthorize("hasAnyRole('TENANT','OWNER')")
    public BookingResponseDto createBooking(CreateBookingDto dto) {

        Long tenantId = extractUserIdFromToken();

        Booking booking = new Booking();
        booking.setRoomId(dto.getRoomId());
        booking.setTenantId(tenantId);
        booking.setOwnerId(dto.getOwnerId());
        booking.setJoinDate(LocalDate.now());
        booking.setStatus(BookingStatus.ACTIVE);

        Booking saved = repository.save(booking);

        BookingEvent event = new BookingEvent(
                saved.getId(),
                saved.getRoomId(),
                saved.getTenantId(),
                "BOOKING_CREATED"
        );

        producer.publishBookingEvent(event);

        return convertToResponse(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER')")
    public BookingResponseDto cancelBooking(Long bookingId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        Booking saved = repository.save(booking);

        BookingEvent event = new BookingEvent(
                saved.getId(),
                saved.getRoomId(),
                saved.getTenantId(),
                "BOOKING_CANCELLED"
        );

        producer.publishBookingEvent(event);

        return convertToResponse(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER')")
    public BookingResponseDto markTenantLeft(Long bookingId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.LEFT);

        Booking saved = repository.save(booking);

        BookingEvent event = new BookingEvent(
                saved.getId(),
                saved.getRoomId(),
                saved.getTenantId(),
                "TENANT_LEFT"
        );

        producer.publishBookingEvent(event);

        return convertToResponse(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER')")
    public List<BookingResponseDto> getBookingsByTenant() {

        Long tenantId = extractUserIdFromToken();

        return repository.findByTenantId(tenantId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER')")
    public List<BookingResponseDto> getBookingsByOwner() {

        Long ownerId = extractUserIdFromToken();

        return repository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private Long extractUserIdFromToken() {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return jwtUtil.extractUserId(token);
    }

    private BookingResponseDto convertToResponse(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setRoomId(booking.getRoomId());
        dto.setTenantId(booking.getTenantId());
        dto.setOwnerId(booking.getOwnerId());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
