package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.dto.CreateRoomResponse;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.Room;
import com.example.smarthomedevicesystemmanagement.service.RoomService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add/{houseId}")
    public ResponseEntity<CreateRoomResponse> createRoom(@PathVariable UUID houseId) {
        return ResponseEntity.ok(roomService.createRoom(houseId));
    }

    @GetMapping("/{houseId}")
    public Page<Room> getAllRooms(
            @PathVariable UUID houseId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be >= 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be >= 1") @Max(value = 50, message = "size must be <= 50")int size
    ) {
        return roomService.getAllRooms(houseId, page, size);
    }

}
