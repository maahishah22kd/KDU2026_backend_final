package com.example.smarthomedevicesystemmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name="device_inventory")
@NoArgsConstructor
public class DeviceInventory {
    @Id
    @Column(name = "kickston_id", nullable = false, length = 6, updatable = false)
    private String kickstonId;

    @Column(name="device_username",nullable = false)
    private String deviceUserName;

    @Column(name="device_password",nullable = false)
    private String devicePassword;

    @Column(name="manufacture_date_time",nullable = false)
    private LocalDateTime manufactureDateTime;

    @Column(name="manufacture_factory_place",nullable = false)
    private String manufactureFactoryPlace;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;
}
