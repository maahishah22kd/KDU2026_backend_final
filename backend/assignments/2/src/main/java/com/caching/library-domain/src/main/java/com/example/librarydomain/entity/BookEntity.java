package com.example.librarydomain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class BookEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected BookEntity() {
        // JPA only
    }

    public BookEntity(String title){
        this.title=title;
        this.status=BookStatus.PROCESSING;
    }


    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public BookStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    public void setTitle(String title) {
        if (title == null || title.trim().length() < 2) {
            throw new IllegalArgumentException("title must be at least 2 characters");
        }
        this.title = title.trim();
    }

    public void setStatus(BookStatus status) {
        if (status == null) throw new IllegalArgumentException("status is required");
        this.status = status;
    }
}
