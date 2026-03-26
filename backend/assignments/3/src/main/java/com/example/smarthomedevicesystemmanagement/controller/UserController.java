package com.example.smarthomedevicesystemmanagement.controller;

import com.example.smarthomedevicesystemmanagement.dto.UserRequest;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.service.HouseMemberService;
import com.example.smarthomedevicesystemmanagement.service.HouseService;
import com.example.smarthomedevicesystemmanagement.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Setter
@Getter
@AllArgsConstructor
public class UserController {
    private final HouseService houseService;
    private final UserService userService;
    private final HouseMemberService houseMemberService;

    @PostMapping("house/{houseId}/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequest req, @PathVariable UUID houseId){
        User user= userService.addUser(req,houseId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public Page<House> getAllHouses(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be >= 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be >= 1") @Max(value = 50, message = "size must be <= 50")int size
    ){
            return houseMemberService.getAllHouses(page,size);
    }

    @PatchMapping("transferAdmin/{houseId}/{userId}")
    public ResponseEntity<?> transferOwnership(@PathVariable UUID userId, @PathVariable UUID houseId){
        User user= userService.tranferOwnership(userId,houseId);
        return ResponseEntity.ok(user);
    }
}
