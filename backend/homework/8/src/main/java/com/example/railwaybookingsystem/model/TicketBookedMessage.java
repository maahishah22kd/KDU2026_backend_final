package com.example.railwaybookingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketBookedMessage {
    private String eventId;
    private String bookingId;
    private String trainId;
    private String seatNo;
    private String phone;
    private int age;
}
