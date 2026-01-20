package com.example.railwaybookingsystem.controller;

import com.example.railwaybookingsystem.model.BookingRequest;
import com.example.railwaybookingsystem.model.BookingResponse;
import com.example.railwaybookingsystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse>book(@RequestBody BookingRequest req){
        BookingResponse response=bookingService.createBookingAndPublish(req);
        return ResponseEntity.accepted().body(response);
    }
}
