package com.example.librarydomain.repo;

import com.example.librarydomain.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {
    Optional<LoanEntity> findByBook_IdAndReturnedAtIsNull(UUID bookId);
}
