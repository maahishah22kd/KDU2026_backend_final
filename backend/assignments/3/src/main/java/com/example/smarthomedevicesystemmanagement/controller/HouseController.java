package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.dto.CreateHouseResponse;
import com.example.smarthomedevicesystemmanagement.dto.HouseAddressResponse;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.service.HouseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/house")
@Getter
@Setter
@AllArgsConstructor
public class HouseController {
    private final HouseService houseService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/add")
    public ResponseEntity<CreateHouseResponse> createHouse(){
        CreateHouseResponse house =houseService.createHouse();
        return ResponseEntity.ok(house);
    }

    @PatchMapping("/{houseId}/address")
    public ResponseEntity<HouseAddressResponse> setAddress(@Valid @RequestBody String address, @PathVariable UUID houseId){
        HouseAddressResponse house= houseService.setAddress(address,houseId);
        return ResponseEntity.ok(house);
    }
}
