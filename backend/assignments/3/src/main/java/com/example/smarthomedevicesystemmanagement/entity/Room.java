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
@Table(name="rooms")
@NoArgsConstructor
public class Room {
    @Id
    @UuidGenerator
    @Column(name="room_id",nullable = false,updatable = false)
    private UUID roomId;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="house_id",nullable = false)
    private House house;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
