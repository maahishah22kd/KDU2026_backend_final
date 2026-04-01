package com.example.libraryservice.service;

import com.example.libraryapi.dto.response.AuditResponse;
import com.example.librarydomain.repo.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final BookRepository bookRepository;

    public AnalyticsService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public AuditResponse auditCountsByStatus() {
        Map<String, Long> counts = bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getStatus().name(),
                        Collectors.counting()
                ));

        return new AuditResponse(counts);
    }
}
