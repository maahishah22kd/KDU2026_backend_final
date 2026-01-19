package com.example.ticketbookingsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class EventEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "event_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "eventname", nullable = false, length = 200)
    private String eventname;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "ticket_count", nullable=false)
    private Integer ticketcount;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected EventEntity() {
        // JPA only
    }

    public EventEntity(String eventname){
        this.eventname=eventname;
    }


    public UUID getId() { return id; }
    public String getTitle() { return eventname; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getVersion() { return version; }
    public Integer getTicketCount() { return ticketcount; }

    public void setTitle(String title) {
        if (title == null || title.trim().length() < 2) {
            throw new IllegalArgumentException("title must be at least 2 characters");
        }
        this.eventname = title.trim();
    }

}
