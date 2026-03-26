package com.pg.booking.service;

import com.pg.booking.dto.BookingResponseDto;
import com.pg.booking.dto.CreateBookingDto;
import com.pg.booking.dto.RoomInternalDto;
import com.pg.booking.entity.Booking;
import com.pg.booking.enums.BookingStatus;
import com.pg.booking.feign.RoomClient;
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
    private final RoomClient roomClient;

    public BookingServiceImpl(BookingRepository repository,
                              BookingEventProducer producer,
                              JwtUtil jwtUtil,
                              HttpServletRequest request,
                              RoomClient roomClient) {
        this.repository = repository;
        this.producer = producer;
        this.jwtUtil = jwtUtil;
        this.request = request;
        this.roomClient=roomClient;
    }

    @Override
    @PreAuthorize("hasAnyRole('TENANT','OWNER')")
    public BookingResponseDto createBooking(CreateBookingDto dto) {

        Long tenantId = extractUserIdFromToken();

        if(repository.existsByTenantIdAndStatus(
                tenantId, BookingStatus.ACTIVE)){
            throw new RuntimeException("You Already have an active booking");
        }

        RoomInternalDto room = roomClient.getRoomById(dto.getRoomId());

        if(room.getAvailableBeds() <= 0){
            throw new RuntimeException("Room is full");
        }

        Booking booking = new Booking();
        booking.setRoomId(dto.getRoomId());
        booking.setTenantId(tenantId);
        booking.setOwnerId(room.getOwnerId());
        booking.setJoinDate(LocalDate.now());
        booking.setStatus(BookingStatus.ACTIVE);

        Booking saved = repository.save(booking);

        // Kafka event
        BookingEvent event = new BookingEvent(
                saved.getId(),
                saved.getRoomId(),
                saved.getTenantId(),
                "BOOKING_CREATED"
        );

        producer.publishBookingEvent(event);

        BookingResponseDto response = convertToResponse(saved);

        // ✅ correct seats (no async issue)
        response.setSeatsLeft(room.getAvailableBeds() - 1);

        return response;
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
    @PreAuthorize("hasAnyRole('TENANT','OWNER')")
    public List<BookingResponseDto> getBookingsByTenant() {

        Long tenantId = extractUserIdFromToken();

        return repository.findByTenantIdAndStatus(tenantId, BookingStatus.ACTIVE)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER')")
    public List<BookingResponseDto> getBookingsByOwner() {

        Long ownerId = extractUserIdFromToken();

        return repository.findByOwnerIdAndStatus(ownerId, BookingStatus.ACTIVE)
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
        dto.setJoinDate(LocalDate.now());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
