package com.pg.service;


import com.pg.dto.CreateRoomDto;
import com.pg.dto.RoomResponseDto;
import com.pg.dto.UpdateRoomDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RoomService {

    RoomResponseDto createRoom(CreateRoomDto dto);

    RoomResponseDto updateRoom(Long roomId, UpdateRoomDto dto);

    void deleteRoom(Long roomId);

    List<RoomResponseDto> getRoomsByOwner();

    List<RoomResponseDto> getAllAvailableRooms();
}