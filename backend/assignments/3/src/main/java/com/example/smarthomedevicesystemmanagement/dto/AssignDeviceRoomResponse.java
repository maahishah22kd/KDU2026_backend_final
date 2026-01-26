package com.example.smarthomedevicesystemmanagement.dto;

import com.example.smarthomedevicesystemmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class AssignDeviceRoomResponse {
    private UUID deviceId;
    private LocalDateTime modifiedAt;
    private UUID RoomId;
}
