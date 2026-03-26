package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.HouseMember;
import com.example.smarthomedevicesystemmanagement.entity.HouseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface HouseMemberRepository extends JpaRepository<HouseMember, UUID> {

    boolean existsByHouse_HouseIdAndUser_UserIdAndRole(UUID houseId, UUID userId, HouseRole role);

    boolean existsByHouse_HouseIdAndUser_UserId(UUID houseId, UUID userId);

    boolean existsByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(UUID houseId, UUID userId);

    boolean existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(UUID houseId, UUID userId, HouseRole role);

    Optional<HouseMember> findByHouse_HouseIdAndUser_UserIdAndDeletedAtIsNull(UUID houseId, UUID userId);

    Optional<HouseMember> findByHouse_HouseIdAndUser_UserId(UUID houseId, UUID userId);

    @Modifying
    @Query("""
        update HouseMember hm
        set hm.deletedAt = :ts, hm.modifiedAt = :ts
        where hm.house.houseId = :houseId and hm.deletedAt is null
    """)
    int softDeleteMembersByHouse(UUID houseId, LocalDateTime ts);

    @Modifying
    @Query("""
        update HouseMember hm
        set hm.deletedAt = :ts, hm.modifiedAt = :ts
        where hm.house.houseId = :houseId and hm.user.userId = :userId and hm.deletedAt is null
    """)
    int softDeleteMember(UUID houseId, UUID userId, LocalDateTime ts);
}
