package com.example.ticketbookingsystem.controller;

import com.example.ticketbookingsystem.dto.CreateEventRequest;
import com.example.ticketbookingsystem.dto.EventResponse;
import com.example.ticketbookingsystem.dto.PagedResponse;
import com.example.ticketbookingsystem.dto.UpdateCatalogRequest;
import com.example.ticketbookingsystem.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     create an event
     */
    @Operation(
            summary = "Create a event (starts in PROCESSING)",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody CreateEventRequest request) {
        return eventService.create(request);
    }

    /**
     * GET /events supports:
     * - optional status
     * - optional titleContains
     * - page, size
     * - sort=createdAt,desc (Spring standard)
     */
    @GetMapping
    public PagedResponse<EventResponse> listEvents(
            @RequestParam(required = false) String titleContains,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        return eventService.search(Optional.ofNullable(titleContains),
                pageable);
    }

    /**
     * Phase 4:
     * PATCH transitions PROCESSING -> AVAILABLE
     */
    @Operation(
            summary = "Catalog an event (PROCESSING -> AVAILABLE)",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PatchMapping("/{id}/catalog")
    public EventResponse updateCatalog(@PathVariable("id") UUID id,
                                      @Valid @RequestBody UpdateCatalogRequest request) throws ChangeSetPersister.NotFoundException {
        return eventService.updateCatalog(id, request);
    }
}
