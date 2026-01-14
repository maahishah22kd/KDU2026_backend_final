package com.example.jpa.Service;

import com.example.jpa.entity.Shift;
import com.example.jpa.Repository.ShiftRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository repository;

    public ShiftService(ShiftRepository repository) {
        this.repository = repository;
    }

    public List<Shift> getTop3NewYearShifts() {

        return repository.findShiftsInDateRange(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 25),
                PageRequest.of(0, 3)
        );
    }
}
