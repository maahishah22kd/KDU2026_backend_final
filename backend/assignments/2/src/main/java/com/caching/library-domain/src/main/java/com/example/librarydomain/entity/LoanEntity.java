package com.example.librarydomain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class LoanEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private UserEntity borrower;

    @Column(name = "borrowed_at", nullable = false)
    private Instant borrowedAt;

    @Column(name = "returned_at")
    private Instant returnedAt;

    protected LoanEntity() {}

    public LoanEntity(BookEntity book, UserEntity borrower, Instant borrowedAt) {
        this.book = book;
        this.borrower = borrower;
        this.borrowedAt = borrowedAt;
    }

    public UUID getId() { return id; }
    public BookEntity getBook() { return book; }
    public UserEntity getBorrower() { return borrower; }
    public Instant getBorrowedAt() { return borrowedAt; }
    public Instant getReturnedAt() { return returnedAt; }

    public boolean isActive() { return returnedAt == null; }
    public void markReturned(Instant returnedAt) { this.returnedAt = returnedAt; }
}
