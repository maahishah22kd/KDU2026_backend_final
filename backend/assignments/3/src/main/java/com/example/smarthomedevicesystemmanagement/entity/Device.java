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
@Table(name="devices",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_devices_kickston_id", columnNames = "kickston_id")
        })
@NoArgsConstructor
public class Device {
    @Id
    @UuidGenerator
    @Column(name="device_id",nullable = false)
    private UUID deviceId;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="kickston_id",nullable = false, unique = true)
    private DeviceInventory deviceInventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User registeredBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
