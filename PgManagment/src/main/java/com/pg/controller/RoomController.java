package com.pg.controller;


import com.pg.dto.CreateRoomDto;
import com.pg.dto.RoomResponseDto;
import com.pg.dto.UpdateRoomDto;

import com.pg.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    // OWNER only (secured in service)
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(
            @RequestBody CreateRoomDto dto) {
        return ResponseEntity.ok(service.createRoom(dto));
    }

    // OWNER only (secured in service)
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(
            @PathVariable Long roomId,
            @RequestBody UpdateRoomDto dto) {
        return ResponseEntity.ok(service.updateRoom(roomId, dto));
    }

    // OWNER only (secured in service)
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long roomId) {
        service.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    // OWNER only (secured in service)
    @GetMapping("/my-rooms")
    public ResponseEntity<List<RoomResponseDto>> getMyRooms() {
        // ownerId extracted inside service
        return ResponseEntity.ok(service.getRoomsByOwner());
    }

    // TENANT + OWNER
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponseDto>> getAvailableRooms() {
        return ResponseEntity.ok(service.getAllAvailableRooms());
    }
}