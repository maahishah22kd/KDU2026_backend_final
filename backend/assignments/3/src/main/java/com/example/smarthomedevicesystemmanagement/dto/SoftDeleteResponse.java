package com.example.smarthomedevicesystemmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SoftDeleteResponse {
    private UUID id;
    private LocalDateTime deletedAt;
}
