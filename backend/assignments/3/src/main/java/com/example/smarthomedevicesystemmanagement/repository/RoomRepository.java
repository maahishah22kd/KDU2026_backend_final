package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    Page<Room> findByHouse_HouseIdAndDeletedAtIsNull(UUID houseId, Pageable pageable);

    Optional<Room> findByRoomIdAndDeletedAtIsNull(UUID roomId);

    Optional<Room> findByRoomIdAndHouse_HouseIdAndDeletedAtIsNull(UUID roomId, UUID houseId);

    @Modifying
    @Query("""
        update Room r
        set r.deletedAt = :ts, r.modifiedAt = :ts
        where r.house.houseId = :houseId and r.deletedAt is null
    """)
    int softDeleteRoomsByHouse(UUID houseId, LocalDateTime ts);

    @Modifying
    @Query("""
        update Room r
        set r.deletedAt = :ts, r.modifiedAt = :ts
        where r.roomId = :roomId and r.house.houseId = :houseId and r.deletedAt is null
    """)
    int softDeleteRoom(UUID houseId, UUID roomId, LocalDateTime ts);
}
