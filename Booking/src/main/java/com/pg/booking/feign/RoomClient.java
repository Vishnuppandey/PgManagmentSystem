package com.pg.booking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pg.booking.dto.RoomInternalDto;
import com.pg.booking.dto.RoomResponseDto;

@FeignClient(name="PGMANAGMENT")
public interface RoomClient {

	@GetMapping("/api/rooms/internal/{id}")
    RoomInternalDto getRoomById(@PathVariable Long id);

}
