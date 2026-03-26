package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.dto.CreateHouseResponse;
import com.example.smarthomedevicesystemmanagement.dto.HouseAddressResponse;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.HouseMember;
import com.example.smarthomedevicesystemmanagement.entity.HouseRole;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.HouseMemberRepository;
import com.example.smarthomedevicesystemmanagement.repository.HouseRepository;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@Slf4j
@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final HouseMemberRepository houseMemberRepository;

    @Transactional
    public CreateHouseResponse createHouse() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("createHouse requested by user={}", username);

        User user = userRepository.findByUserNameAndDeletedAtIsNull(username)
                .orElseThrow(() -> {
                    log.warn("createHouse failed: user not found user={}", username);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        House house = new House();
        house.setCreatedAt(LocalDateTime.now());
        House savedHouse = houseRepository.save(house);

        HouseMember hm = new HouseMember();
        hm.setCreatedAt(LocalDateTime.now());
        hm.setHouse(savedHouse);
        hm.setRole(HouseRole.ADMIN);
        hm.setUser(user);

        houseMemberRepository.save(hm);

        log.info("createHouse success houseId={} adminUser={}", savedHouse.getHouseId(), username);

        return new CreateHouseResponse(savedHouse.getHouseId(), savedHouse.getCreatedAt());
    }

    @Transactional
    public HouseAddressResponse setAddress(String address, UUID houseId) {

        log.info("setAddress requested houseId={} addressLength={}", houseId, address == null ? 0 : address.length());

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> {
                    log.warn("setAddress failed: house not found houseId={}", houseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
                });

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("setAddress failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String currentUsername = auth.getName();
        log.info("setAddress attempt houseId={} by user={}", houseId, currentUsername);

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("setAddress failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(houseId, currentUser.getUserId(), HouseRole.ADMIN);

        if (!isAdmin) {
            log.warn("setAddress forbidden houseId={} user={}", houseId, currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        house.setAddress(address);
        house.setModifiedAt(LocalDateTime.now());
        House savedHouse = houseRepository.save(house);

        houseRepository.save(savedHouse);

        log.info("setAddress success houseId={} by user={}", houseId, currentUsername);

        return new HouseAddressResponse(
                savedHouse.getHouseId(),
                savedHouse.getModifiedAt(),
                savedHouse.getAddress()
        );
    }
}
