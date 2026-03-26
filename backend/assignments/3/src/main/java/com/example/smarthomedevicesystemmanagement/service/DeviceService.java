package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.dto.*;
import com.example.smarthomedevicesystemmanagement.entity.*;
import com.example.smarthomedevicesystemmanagement.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final UserRepository userRepository;
    private final DeviceInventoryRepository deviceInventoryRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final HouseRepository houseRepository;
    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public CreateDeviceResponse addDevice(UUID houseId, CreateDeviceRequest req) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("addDevice failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String currentUsername = auth.getName();
        log.info("addDevice requested houseId={} kickstonId={} by user={}", houseId, req.getKickstonId(), currentUsername);

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("addDevice failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> {
                    log.warn("addDevice failed: house not found houseId={}", houseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
                });

        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(houseId, currentUser.getUserId(), HouseRole.ADMIN);

        if (!isAdmin) {
            log.warn("addDevice forbidden: not admin houseId={} user={}", houseId, currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        DeviceInventory deviceInventory = deviceInventoryRepository.findById(req.getKickstonId())
                .orElseThrow(() -> {
                    log.warn("addDevice failed: inventory device not found kickstonId={}", req.getKickstonId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
                });

        if (!req.getDeviceUsername().equals(deviceInventory.getDeviceUserName())) {
            log.warn("addDevice failed: incorrect device username kickstonId={}", req.getKickstonId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect user name");
        }

        if (!req.getDevicePassword().equals(deviceInventory.getDevicePassword())) {
            log.warn("addDevice failed: incorrect device password kickstonId={}", req.getKickstonId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        boolean alreadyRegistered =
                deviceRepository.existsByDeviceInventory_KickstonIdAndDeletedAtIsNull(req.getKickstonId());

        if (alreadyRegistered) {
            log.warn("addDevice conflict: already registered kickstonId={}", req.getKickstonId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Device already registered to a house");
        }

        Device device = new Device();
        device.setRoom(null);
        device.setHouse(house);
        device.setCreatedAt(LocalDateTime.now());
        device.setRegisteredBy(currentUser);
        device.setDeviceInventory(deviceInventory);

        Device saved = deviceRepository.save(device);

        log.info("addDevice success deviceId={} kickstonId={} houseId={} by user={}",
                saved.getDeviceId(), req.getKickstonId(), houseId, currentUsername);

        return new CreateDeviceResponse(saved.getDeviceId(), saved.getCreatedAt(), saved.getRegisteredBy().getUserName());
    }

    @Transactional
    public AssignDeviceRoomResponse addDeviceToRoom(UUID roomId, UUID deviceId) {

        log.info("addDeviceToRoom requested roomId={} deviceId={}", roomId, deviceId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.warn("addDeviceToRoom failed: room not found roomId={}", roomId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
                });

        House house = room.getHouse();

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("addDeviceToRoom failed: authentication missing roomId={} deviceId={}", roomId, deviceId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String currentUsername = auth.getName();

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("addDeviceToRoom failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(house.getHouseId(), currentUser.getUserId(), HouseRole.ADMIN);

        if (!isAdmin) {
            log.warn("addDeviceToRoom forbidden houseId={} user={}", house.getHouseId(), currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> {
                    log.warn("addDeviceToRoom failed: device not found deviceId={}", deviceId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
                });

        if (!house.getHouseId().equals(device.getHouse().getHouseId())) {
            log.warn("addDeviceToRoom bad request: room house != device house roomId={} deviceId={}", roomId, deviceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room and device do not belong to the same house");
        }

        device.setRoom(room);
        device.setModifiedAt(LocalDateTime.now());
        Device saved = deviceRepository.save(device);

        log.info("addDeviceToRoom success deviceId={} roomId={} houseId={}",
                saved.getDeviceId(), saved.getRoom().getRoomId(), house.getHouseId());

        return new AssignDeviceRoomResponse(saved.getDeviceId(), saved.getModifiedAt(), saved.getRoom().getRoomId());
    }

    @Transactional
    public AssignDeviceRoomResponse moveDeviceToRoom(UUID sourceRoomId, UUID deviceId, UUID targetRoomId) {

        log.info("moveDeviceToRoom requested deviceId={} sourceRoomId={} targetRoomId={}",
                deviceId, sourceRoomId, targetRoomId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("moveDeviceToRoom failed: authentication missing");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String userName = auth.getName();

        User user = userRepository.findByUserNameAndDeletedAtIsNull(userName)
                .orElseThrow(() -> {
                    log.warn("moveDeviceToRoom failed: user not found user={}", userName);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        Room sourceRoom = roomRepository.findById(sourceRoomId)
                .orElseThrow(() -> {
                    log.warn("moveDeviceToRoom failed: source room not found sourceRoomId={}", sourceRoomId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Source room not found");
                });

        Room targetRoom = roomRepository.findById(targetRoomId)
                .orElseThrow(() -> {
                    log.warn("moveDeviceToRoom failed: target room not found targetRoomId={}", targetRoomId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Target room not found");
                });

        House house = sourceRoom.getHouse();
        if (!house.getHouseId().equals(targetRoom.getHouse().getHouseId())) {
            log.warn("moveDeviceToRoom bad request: rooms not in same house sourceRoomId={} targetRoomId={}",
                    sourceRoomId, targetRoomId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rooms do not belong to the same house");
        }

        boolean isMember = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(house.getHouseId(), user.getUserId());

        if (!isMember) {
            log.warn("moveDeviceToRoom forbidden: user not in house user={} houseId={}", userName, house.getHouseId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not belong to this house");
        }

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> {
                    log.warn("moveDeviceToRoom failed: device not found deviceId={}", deviceId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
                });

        if (!device.getHouse().getHouseId().equals(house.getHouseId())) {
            log.warn("moveDeviceToRoom bad request: device not in house deviceId={} houseId={}", deviceId, house.getHouseId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device does not belong to this house");
        }

        if (device.getRoom() == null) {
            log.warn("moveDeviceToRoom bad request: device has no room deviceId={}", deviceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device is not assigned to any room");
        }

        if (!device.getRoom().getRoomId().equals(sourceRoomId)) {
            log.warn("moveDeviceToRoom bad request: device not in source room deviceId={} sourceRoomId={}", deviceId, sourceRoomId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device does not belong to the source room");
        }

        device.setRoom(targetRoom);
        device.setModifiedAt(LocalDateTime.now());

        Device saved = deviceRepository.save(device);

        log.info("moveDeviceToRoom success deviceId={} newRoomId={} houseId={}",
                saved.getDeviceId(), saved.getRoom().getRoomId(), house.getHouseId());

        return new AssignDeviceRoomResponse(saved.getDeviceId(), saved.getModifiedAt(), saved.getRoom().getRoomId());
    }

    public Page<RoomWithDevicesResponse> getRoomsWithDevices(UUID houseId, int page, int size) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("getRoomsWithDevices failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String userName = auth.getName();
        log.info("getRoomsWithDevices requested houseId={} page={} size={} by user={}", houseId, page, size, userName);

        User user = userRepository.findByUserNameAndDeletedAtIsNull(userName)
                .orElseThrow(() -> {
                    log.warn("getRoomsWithDevices failed: user not found user={}", userName);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        boolean isMember = houseMemberRepository.existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(houseId, user.getUserId());
        if (!isMember) {
            log.warn("getRoomsWithDevices forbidden houseId={} user={}", houseId, userName);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this house");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Room> roomsPage = roomRepository.findByHouse_HouseIdAndDeletedAtIsNull(houseId, pageable);

        List<UUID> roomIds = roomsPage.getContent().stream()
                .map(Room::getRoomId)
                .toList();

        List<Device> devices = roomIds.isEmpty()
                ? List.of()
                : deviceRepository.findByHouse_HouseIdAndRoom_RoomIdInAndDeletedAtIsNull(houseId, roomIds);

        Map<UUID, List<Device>> devicesByRoomId = devices.stream()
                .filter(d -> d.getRoom() != null)
                .collect(Collectors.groupingBy(d -> d.getRoom().getRoomId()));

        List<RoomWithDevicesResponse> content = roomsPage.getContent().stream()
                .map(room -> {
                    List<DeviceSummary> deviceSummaries = devicesByRoomId
                            .getOrDefault(room.getRoomId(), List.of())
                            .stream()
                            .map(d -> new DeviceSummary(
                                    d.getDeviceId(),
                                    d.getDeviceInventory() != null ? d.getDeviceInventory().getKickstonId() : null,
                                    d.getRegisteredBy() != null ? d.getRegisteredBy().getUserName() : null,
                                    d.getCreatedAt(),
                                    d.getModifiedAt()
                            ))
                            .toList();

                    return new RoomWithDevicesResponse(
                            room.getRoomId(),
                            room.getCreatedAt(),
                            room.getModifiedAt(),
                            deviceSummaries
                    );
                })
                .toList();

        log.info("getRoomsWithDevices success houseId={} roomsReturned={} devicesFetched={}",
                houseId, roomsPage.getNumberOfElements(), devices.size());

        return new PageImpl<>(content, pageable, roomsPage.getTotalElements());
    }
}
