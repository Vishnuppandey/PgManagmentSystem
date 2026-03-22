package com.pg.service;


import com.pg.dto.*;
import com.pg.entity.Room;
import com.pg.enums.RoomStatus;
import com.pg.repository.RoomRepository;
import com.pg.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public RoomServiceImpl(RoomRepository repository,
                           JwtUtil jwtUtil,
                           HttpServletRequest request) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    // ✅ OWNER only
    @Override
    @PreAuthorize("hasRole('OWNER')")
    public RoomResponseDto createRoom(CreateRoomDto dto) {

        Long ownerId = extractUserIdFromToken();
        if(repository.existsByOwnerIdAndRoomNumber(ownerId, dto.getRoomNumber())){
            throw new RuntimeException("Room already exists for this owner");
        }

        Room room = convertToEntity(dto);
        room.setOwnerId(ownerId);
        room.setAvailableBeds(dto.getCapacity());
        room.setStatus(RoomStatus.AVAILABLE);

        return convertToResponse(repository.save(room));
    }

    @Override
    @PreAuthorize("hasRole('OWNER')")
    public RoomResponseDto updateRoom(Long roomId, UpdateRoomDto dto) {

        Long ownerId = extractUserIdFromToken();

        Room room = repository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Not your room");
        }

        room.setRent(dto.getRent());
        room.setStatus(dto.getStatus());
        room.setActive(dto.getActive());

        return convertToResponse(repository.save(room));
    }

    @Override
    @PreAuthorize("hasRole('OWNER')")
    public void deleteRoom(Long roomId) {

        Long ownerId = extractUserIdFromToken();

        Room room = repository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Not your room");
        }

        repository.delete(room);
    }

    @Override
    @PreAuthorize("hasRole('OWNER')")
    public List<RoomResponseDto> getRoomsByOwner() {
        Long ownerId=extractUserIdFromToken();
        System.out.println(ownerId);
        List<Room> rooms=repository.findByOwnerId(ownerId);
        List<RoomResponseDto> dto=new ArrayList<>();
        for(Room room:rooms){
            dto.add(convertToResponse(room));
        }
        return dto;
    }

    // TENANT + OWNER both can view
    @Override
    public List<RoomResponseDto> getAllAvailableRooms() {
         List<Room> rooms=repository.findByStatusAndActiveTrue(RoomStatus.AVAILABLE);
         List<RoomResponseDto> dto=new ArrayList<>();
         for(Room room:rooms){
             dto.add(convertToResponse(room));
         }
         return dto;
    }

    @Override
	public RoomInternalDto findInternalyRoom(Long roomId) {
		Room room=repository.findById(roomId).orElseThrow(()->new RuntimeException("room not found"));
		RoomInternalDto dto=new RoomInternalDto();
		dto.setAvailableBeds(room.getAvailableBeds());
		dto.setId(room.getId());
		dto.setOwnerId(room.getOwnerId());
		dto.setStatus(room.getStatus());
		return dto;
	}
    
    private Long extractUserIdFromToken() {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }

    private Room convertToEntity(CreateRoomDto dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setSharingType(dto.getSharingType());
        room.setCapacity(dto.getCapacity());
        room.setRent(dto.getRent());
        return room;
    }

    private RoomResponseDto convertToResponse(Room room) {
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setSharingType(room.getSharingType());
        dto.setCapacity(room.getCapacity());
        dto.setAvailableBeds(room.getAvailableBeds());
        dto.setRent(room.getRent());
        dto.setStatus(room.getStatus());
        return dto;
    }

	
}