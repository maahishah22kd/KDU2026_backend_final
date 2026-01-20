package com.example.railwaybookingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private String passengerName;
    private int age;
    private String phone;
    private String trainId;
    private String seatNo;
}
