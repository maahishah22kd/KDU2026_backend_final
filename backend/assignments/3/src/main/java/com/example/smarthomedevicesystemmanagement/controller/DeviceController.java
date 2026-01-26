package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.dto.*;
import com.example.smarthomedevicesystemmanagement.entity.Device;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.Room;
import com.example.smarthomedevicesystemmanagement.service.DeviceService;
import com.example.smarthomedevicesystemmanagement.service.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/add/{houseId}")
    public ResponseEntity<CreateDeviceResponse> addDevice(@PathVariable UUID houseId, @Valid @RequestBody CreateDeviceRequest req) {
        return ResponseEntity.ok(deviceService.addDevice(houseId,req));
    }

    @PutMapping("/add/{roomId}/{deviceId}")
    public ResponseEntity<AssignDeviceRoomResponse> addDeviceToRoom(@PathVariable UUID roomId, @PathVariable UUID deviceId){
        return ResponseEntity.ok(deviceService.addDeviceToRoom(roomId,deviceId));
    }

    @PutMapping("/{deviceId}/move/{roomId1}/to/{roomId2}")
    public ResponseEntity<AssignDeviceRoomResponse> moveDeviceToRoom(@PathVariable UUID roomId1, @PathVariable UUID deviceId, @PathVariable UUID roomId2){
        return ResponseEntity.ok(deviceService.moveDeviceToRoom(roomId1,deviceId,roomId2));
    }

    @GetMapping("/house/{houseId}/rooms-with-devices")
    public Page<RoomWithDevicesResponse> getRoomsWithDevices(
            @PathVariable UUID houseId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be >= 0")int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be >= 1") @Max(value = 50, message = "size must be <= 50")int size
    ) {
        return deviceService.getRoomsWithDevices(houseId, page, size);
    }

}
