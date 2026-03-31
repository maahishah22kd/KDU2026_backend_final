package com.example.jpa.Repository;

import com.example.jpa.entity.Shift;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("""
        select s
        from Shift s
        where s.startDate >= :startDate
          and s.endDate <= :endDate
        order by s.shiftName asc
    """)


    List<Shift> findShiftsInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
