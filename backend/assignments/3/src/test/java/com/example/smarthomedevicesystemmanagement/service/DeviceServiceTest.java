package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.entity.*;
import com.example.smarthomedevicesystemmanagement.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DeviceServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private DeviceInventoryRepository deviceInventoryRepository;
    @Mock private HouseMemberRepository houseMemberRepository;
    @Mock private HouseRepository houseRepository;
    @Mock private DeviceRepository deviceRepository;
    @Mock private RoomRepository roomRepository;

    @InjectMocks private DeviceService deviceService;

    @BeforeEach
    void setupAuth() {
        var auth = new UsernamePasswordAuthenticationToken("maahi", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void moveDeviceToRoom_success() {
        UUID sourceRoomId = UUID.randomUUID();
        UUID targetRoomId = UUID.randomUUID();
        UUID deviceId = UUID.randomUUID();
        UUID houseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        user.setUserName("maahi");

        House house = new House();
        house.setHouseId(houseId);

        Room sourceRoom = new Room();
        sourceRoom.setRoomId(sourceRoomId);
        sourceRoom.setHouse(house);

        Room targetRoom = new Room();
        targetRoom.setRoomId(targetRoomId);
        targetRoom.setHouse(house);

        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setHouse(house);
        device.setRoom(sourceRoom);
        device.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByUserNameAndDeletedAtIsNull("maahi")).thenReturn(Optional.of(user));
        when(roomRepository.findById(sourceRoomId)).thenReturn(Optional.of(sourceRoom));
        when(roomRepository.findById(targetRoomId))
                .thenReturn(Optional.of(targetRoom));
        when(houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(houseId, userId))
                .thenReturn(true);
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));

        var resp = deviceService.moveDeviceToRoom(sourceRoomId, deviceId, targetRoomId);

        assertEquals(deviceId, resp.getDeviceId());
        assertEquals(targetRoomId, resp.getRoomId());
    }

    @Test
    void moveDeviceToRoom_forbidden_if_not_member() {
        UUID sourceRoomId = UUID.randomUUID();
        UUID targetRoomId = UUID.randomUUID();
        UUID deviceId = UUID.randomUUID();
        UUID houseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        user.setUserName("maahi");

        House house = new House();
        house.setHouseId(houseId);

        Room sourceRoom = new Room();
        sourceRoom.setRoomId(sourceRoomId);
        sourceRoom.setHouse(house);

        Room targetRoom = new Room();
        targetRoom.setRoomId(targetRoomId);
        targetRoom.setHouse(house);

        when(userRepository.findByUserNameAndDeletedAtIsNull("maahi")).thenReturn(Optional.of(user));
        when(roomRepository.findById(sourceRoomId)).thenReturn(Optional.of(sourceRoom));
        when(roomRepository.findById(targetRoomId))
                .thenReturn(Optional.of(targetRoom));
        when(houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(houseId, userId))
                .thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> deviceService.moveDeviceToRoom(sourceRoomId, deviceId, targetRoomId));

        assertEquals(403, ex.getStatusCode().value());
    }

    // Helper to avoid some static import confusion in some CI envs; resolves to roomRepository::findById
    private static Answer<Optional<Room>> room_repository_findById(RoomRepository repo) {
        return invocation -> Optional.empty();
    }
}
