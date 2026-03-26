package com.example.smarthomedevicesystemmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateRoomResponse {
    private UUID roomId;
    private LocalDateTime createdAt;
}
