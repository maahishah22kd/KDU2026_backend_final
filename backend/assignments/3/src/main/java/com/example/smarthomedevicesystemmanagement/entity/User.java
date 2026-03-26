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
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_name", columnNames = {"user_name"})
        })
@NoArgsConstructor
public class User {
    @Id
    @UuidGenerator
    @Column(name="user_id", nullable = false)
    private UUID userId;

    @Column(name="user_name",nullable = false)
    private String userName;

    @Column(name="password_hash",nullable = false)
    private String password;

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
