package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.dto.SoftDeleteResponse;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.HouseRole;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SoftDeleteService {

    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final HouseMemberRepository houseMemberRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return userRepository.findByUserNameAndDeletedAtIsNull(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private void requireHouseAdmin(UUID houseId, UUID userId) {
        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(houseId, userId, HouseRole.ADMIN);

        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }
    }

    @Transactional
    public SoftDeleteResponse softDeleteHouse(UUID houseId) {
        User me = currentUser();

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found"));

        requireHouseAdmin(house.getHouseId(), me.getUserId());

        LocalDateTime ts = LocalDateTime.now();

        roomRepository.softDeleteRoomsByHouse(houseId, ts);
        deviceRepository.softDeleteDevicesByHouse(houseId, ts);
        houseMemberRepository.softDeleteMembersByHouse(houseId, ts);

        int updated = houseRepository.softDeleteHouse(houseId, ts);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
        }

        return new SoftDeleteResponse(houseId, ts);
    }

    @Transactional
    public SoftDeleteResponse softDeleteRoom(UUID houseId, UUID roomId) {
        User me = currentUser();

        houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found"));

        requireHouseAdmin(houseId, me.getUserId());

        roomRepository.findByRoomIdAndHouse_HouseIdAndDeletedAtIsNull(roomId, houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        LocalDateTime ts = LocalDateTime.now();

        deviceRepository.unassignDevicesFromRoom(houseId, roomId, ts);

        int updated = roomRepository.softDeleteRoom(houseId, roomId, ts);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }

        return new SoftDeleteResponse(roomId, ts);
    }


    @Transactional
    public SoftDeleteResponse softDeleteDevice(UUID houseId, UUID deviceId) {
        User me = currentUser();

        houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found"));

        requireHouseAdmin(houseId, me.getUserId());

        deviceRepository.findByDeviceIdAndHouse_HouseIdAndDeletedAtIsNull(deviceId, houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        LocalDateTime ts = LocalDateTime.now();

        int updated = deviceRepository.softDeleteDevice(houseId, deviceId, ts);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
        }

        return new SoftDeleteResponse(deviceId, ts);
    }

    @Transactional
    public SoftDeleteResponse removeMember(UUID houseId, UUID memberUserId) {
        User me = currentUser();

        houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found"));

        requireHouseAdmin(houseId, me.getUserId());

        if (me.getUserId().equals(memberUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin cannot remove self");
        }

        boolean exists = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(houseId, memberUserId);

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of this house");
        }

        LocalDateTime ts = LocalDateTime.now();

        int updated = houseMemberRepository.softDeleteMember(houseId, memberUserId, ts);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of this house");
        }

        return new SoftDeleteResponse(memberUserId, ts);
    }
}
