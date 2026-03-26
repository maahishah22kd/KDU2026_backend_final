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
@Table(name="houses")
@NoArgsConstructor
public class House {
    @Id
    @UuidGenerator
    @Column(name="house_id",nullable = false,updatable = false)
    private UUID houseId;

    @Column(name="address")
    private String address;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
