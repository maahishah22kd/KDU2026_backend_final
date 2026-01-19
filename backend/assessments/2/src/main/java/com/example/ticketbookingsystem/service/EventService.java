package com.example.ticketbookingsystem.service;

import com.example.ticketbookingsystem.dto.CreateEventRequest;
import com.example.ticketbookingsystem.dto.EventResponse;
import com.example.ticketbookingsystem.dto.PagedResponse;
import com.example.ticketbookingsystem.dto.UpdateCatalogRequest;
import com.example.ticketbookingsystem.entity.EventEntity;
import com.example.ticketbookingsystem.repository.EventRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for event lifecycle operations (create, catalog, retrieve, search).
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Phase 4:
     * POST /books creates a book in PROCESSING.
     */
    @Transactional
    public EventResponse create(CreateEventRequest req) {
        // BookEntity constructor should set default status to PROCESSING (recommended),
        // but we force it here to be safe even if constructor changes.
        EventEntity book = new EventEntity(req.getTitle());

        EventEntity saved = eventRepository.save(book);
        return toResponse(saved);
    }

    /**
     * Basic listing (used earlier).
     */
    @Transactional(readOnly = true)
    public List<EventResponse> listAll() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Phase 4:
     * PATCH /events/{id}/catalog transitions PROCESSING -> AVAILABLE
     * else 409 INVALID_STATE_TRANSITION.
     *
     * Also includes an explicit version check (optimistic-style) to support concurrency safety.
     */

    @Transactional
    public EventResponse updateCatalog(UUID id, UpdateCatalogRequest req) throws ChangeSetPersister.NotFoundException {
        EventEntity book = eventRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        // Update catalog details + status transition
        EventEntity saved = eventRepository.save(book);
        return toResponse(saved);
    }

    /**
     * Phase 7:
     * GET /books with optional status, optional titleContains, pageable (page/size/sort).
     */
    @Transactional(readOnly = true)
    public PagedResponse<EventResponse> search(
                                             Optional<String> titleContains,
                                             Pageable pageable) {

        Specification<EventEntity> spec = Specification.where(null);

        Page<EventEntity> page = eventRepository.findAll(spec, pageable);

        return new PagedResponse<>(
                page.getContent().stream().map(this::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    /**
     * Entity -> DTO mapping (Streams requirement).
     * Keep this in service to avoid exposing entities to web layer.
     */
    private EventResponse toResponse(EventEntity b) {
        return new EventResponse(
                b.getId(),
                b.getTitle(),
                b.getCreatedAt(),
                b.getVersion()
        );
    }
}
