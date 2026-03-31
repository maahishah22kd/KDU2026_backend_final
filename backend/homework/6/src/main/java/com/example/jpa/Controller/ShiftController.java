package com.example.jpa.Controller;

import com.example.jpa.entity.Shift;
import com.example.jpa.Service.ShiftService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService service;

    public ShiftController(ShiftService service) {
        this.service = service;
    }

    @GetMapping("/new-year")
    public List<Shift> getNewYearShifts() {
        return service.getTop3NewYearShifts();
    }
}
