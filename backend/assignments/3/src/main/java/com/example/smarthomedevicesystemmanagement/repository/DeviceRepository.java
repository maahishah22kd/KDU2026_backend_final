package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByHouse_HouseIdAndRoom_RoomIdInAndDeletedAtIsNull(UUID houseId, Collection<UUID> roomIds);

    List<Device> findByHouse_HouseIdAndRoomIsNullAndDeletedAtIsNull(UUID houseId);

    boolean existsByDeviceInventory_KickstonIdAndDeletedAtIsNull(String kickstonId);

    Optional<Device> findByDeviceIdAndDeletedAtIsNull(UUID deviceId);

    Optional<Device> findByDeviceIdAndHouse_HouseIdAndDeletedAtIsNull(UUID deviceId, UUID houseId);

    @Modifying
    @Query("""
        update Device d
        set d.deletedAt = :ts, d.modifiedAt = :ts, d.room = null
        where d.house.houseId = :houseId and d.deletedAt is null
    """)
    int softDeleteDevicesByHouse(UUID houseId, LocalDateTime ts);

    @Modifying
    @Query("""
        update Device d
        set d.deletedAt = :ts, d.modifiedAt = :ts, d.room = null
        where d.deviceId = :deviceId and d.house.houseId = :houseId and d.deletedAt is null
    """)
    int softDeleteDevice(UUID houseId, UUID deviceId, LocalDateTime ts);

    @Modifying
    @Query("""
    update Device d
    set d.room = null, d.modifiedAt = :ts
    where d.house.houseId = :houseId
      and d.room.roomId = :roomId
      and d.deletedAt is null
""")
    int unassignDevicesFromRoom(UUID houseId, UUID roomId, LocalDateTime ts);

}
