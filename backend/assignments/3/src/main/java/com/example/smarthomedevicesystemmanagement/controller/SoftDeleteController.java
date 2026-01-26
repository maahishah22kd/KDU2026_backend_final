package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.dto.SoftDeleteResponse;
import com.example.smarthomedevicesystemmanagement.service.SoftDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delete")
public class SoftDeleteController {

    private final SoftDeleteService softDeleteService;

    @DeleteMapping("/house/{houseId}")
    public ResponseEntity<SoftDeleteResponse> deleteHouse(@PathVariable UUID houseId) {
        return ResponseEntity.ok(softDeleteService.softDeleteHouse(houseId));
    }

    @DeleteMapping("/house/{houseId}/room/{roomId}")
    public ResponseEntity<SoftDeleteResponse> deleteRoom(@PathVariable UUID houseId, @PathVariable UUID roomId) {
        return ResponseEntity.ok(softDeleteService.softDeleteRoom(houseId, roomId));
    }

    @DeleteMapping("/house/{houseId}/device/{deviceId}")
    public ResponseEntity<SoftDeleteResponse> deleteDevice(@PathVariable UUID houseId, @PathVariable UUID deviceId) {
        return ResponseEntity.ok(softDeleteService.softDeleteDevice(houseId, deviceId));
    }

    @DeleteMapping("/house/{houseId}/member/{userId}")
    public ResponseEntity<SoftDeleteResponse> removeMember(@PathVariable UUID houseId, @PathVariable UUID userId) {
        return ResponseEntity.ok(softDeleteService.removeMember(houseId, userId));
    }
}
