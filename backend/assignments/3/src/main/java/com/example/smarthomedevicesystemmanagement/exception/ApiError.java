package com.example.smarthomedevicesystemmanagement.exception;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}
