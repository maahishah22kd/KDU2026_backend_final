package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.dto.CreateRoomResponse;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.HouseRole;
import com.example.smarthomedevicesystemmanagement.entity.Room;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.HouseMemberRepository;
import com.example.smarthomedevicesystemmanagement.repository.HouseRepository;
import com.example.smarthomedevicesystemmanagement.repository.RoomRepository;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public CreateRoomResponse createRoom(UUID houseId) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("createRoom failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String currentUsername = auth.getName();
        log.info("createRoom requested houseId={} by user={}", houseId, currentUsername);

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("createRoom failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> {
                    log.warn("createRoom failed: house not found houseId={}", houseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
                });

        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(houseId, currentUser.getUserId(), HouseRole.ADMIN);

        if (!isAdmin) {
            log.warn("createRoom forbidden houseId={} user={}", houseId, currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        Room room = new Room();
        room.setHouse(house);
        room.setCreatedAt(LocalDateTime.now());
        room.setModifiedAt(null);
        room.setDeletedAt(null);

        Room saved = roomRepository.save(room);

        log.info("createRoom success roomId={} houseId={} by user={}", saved.getRoomId(), houseId, currentUsername);

        return new CreateRoomResponse(saved.getRoomId(), saved.getCreatedAt());
    }

    public Page<Room> getAllRooms(UUID houseId, int page, int size) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("getAllRooms failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String userName = auth.getName();
        log.info("getAllRooms requested houseId={} page={} size={} by user={}", houseId, page, size, userName);

        User user = userRepository.findByUserNameAndDeletedAtIsNull(userName)
                .orElseThrow(() -> {
                    log.warn("getAllRooms failed: user not found user={}", userName);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        boolean isMember = houseMemberRepository.existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(houseId, user.getUserId());
        if (!isMember) {
            log.warn("getAllRooms forbidden houseId={} user={}", houseId, userName);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this house");
        }

        Pageable pageable = PageRequest.of(page, size);
        return roomRepository.findByHouse_HouseIdAndDeletedAtIsNull(houseId, pageable);
    }
}
