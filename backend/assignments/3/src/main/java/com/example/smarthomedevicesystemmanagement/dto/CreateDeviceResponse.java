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
public class CreateDeviceResponse {
    private UUID deviceId;
    private LocalDateTime createdAt;
    private String createdBy;
}
