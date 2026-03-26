package com.example.smarthomedevicesystemmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name="house_members",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_house_members_house_user", columnNames = {"house_id", "user_id"})
        })
@NoArgsConstructor
public class HouseMember {
    @Id
    @UuidGenerator
    @Column(name="house_member_id", nullable = false,updatable = false)
    private UUID houseMemberId;

    @Column(name="role",nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseRole role;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="house_id",nullable = false)
    private House house;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
