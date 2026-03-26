package com.example.smarthomedevicesystemmanagement.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RoomWithDevicesResponse(
        UUID roomId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        List<DeviceSummary> devices
) {}
