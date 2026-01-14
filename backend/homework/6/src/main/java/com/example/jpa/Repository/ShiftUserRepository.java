package com.example.jpa.Repository;

import com.example.jpa.entity.ShiftUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftUserRepository extends JpaRepository<ShiftUser, Long> {
}
