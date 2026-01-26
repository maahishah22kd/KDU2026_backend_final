package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {

    @Query("""
        select h
        from House h
        join HouseMember hm on hm.house = h
        where hm.user.userId = :userId
          and hm.deletedAt is null
          and h.deletedAt is null
    """)
    Page<House> findAllByMemberUserId(@Param("userId") UUID userId, Pageable pageable);

    Optional<House> findByHouseIdAndDeletedAtIsNull(UUID houseId);

    @Modifying
    @Query("""
        update House h
        set h.deletedAt = :ts, h.modifiedAt = :ts
        where h.houseId = :houseId and h.deletedAt is null
    """)
    int softDeleteHouse(UUID houseId, LocalDateTime ts);
}
