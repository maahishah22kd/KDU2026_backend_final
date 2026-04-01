package com.example.libraryservice.service;

import com.example.libraryapi.dto.request.CreateBookRequest;
import com.example.libraryapi.dto.request.UpdateCatalogRequest;
import com.example.libraryapi.dto.response.BookResponse;
import com.example.libraryapi.dto.response.PagedResponse;
import com.example.librarydomain.entity.BookEntity;
import com.example.librarydomain.entity.BookStatus;
import com.example.librarydomain.repo.BookRepository;
import com.example.librarydomain.repo.BookSpecifications;
import com.example.libraryservice.aop.AuditAction;
import com.example.libraryservice.exception.ConflictException;
import com.example.libraryservice.exception.InvalidStateTransitionException;
import com.example.libraryservice.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for book lifecycle operations (create, catalog, retrieve, search).
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Enforce domain rules for status transitions (e.g., PROCESSING → AVAILABLE).</li>
 *   <li>Translate persistence entities to API-facing DTOs.</li>
 *   <li>Support pagination and dynamic search via specifications.</li>
 * </ul>
 *
 * <p>Concurrency:</p>
 * <ul>
 *   <li>Relies on optimistic locking (JPA @Version) to detect concurrent updates.</li>
 * </ul>
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Phase 4:
     * POST /books creates a book in PROCESSING.
     */
    @AuditAction("create")
    @Transactional
    public BookResponse create(CreateBookRequest req) {
        // BookEntity constructor should set default status to PROCESSING (recommended),
        // but we force it here to be safe even if constructor changes.
        BookEntity book = new BookEntity(req.title());
        book.setStatus(BookStatus.PROCESSING);

        BookEntity saved = bookRepository.save(book);
        return toResponse(saved);
    }

    /**
     * Basic listing (used earlier).
     */
    @Transactional(readOnly = true)
    public List<BookResponse> listAll() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Phase 4:
     * PATCH /books/{id}/catalog transitions PROCESSING -> AVAILABLE
     * else 409 INVALID_STATE_TRANSITION.
     *
     * Also includes an explicit version check (optimistic-style) to support concurrency safety.
     */

    @AuditAction("catalog")
    @Transactional
    public BookResponse updateCatalog(UUID id, UpdateCatalogRequest req) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        // Optional: explicit version mismatch => 409 (Conflict)
        if (req.version() != null && book.getVersion() != null && !book.getVersion().equals(req.version())) {
            throw new ConflictException(
                    "Version mismatch. Current=" + book.getVersion() + ", Provided=" + req.version()
            );
        }

        // Phase 4 rule: only PROCESSING -> AVAILABLE
        if (book.getStatus() != BookStatus.PROCESSING) {
            throw new InvalidStateTransitionException(
                    "Cannot transition " + book.getStatus() + " -> AVAILABLE"
            );
        }

        // Update catalog details + status transition
        book.setTitle(req.title());
        book.setStatus(BookStatus.AVAILABLE);

        BookEntity saved = bookRepository.save(book);
        return toResponse(saved);
    }

    /**
     * Phase 7:
     * GET /books with optional status, optional titleContains, pageable (page/size/sort).
     */
    @Transactional(readOnly = true)
    public PagedResponse<BookResponse> search(Optional<BookStatus> status,
                                              Optional<String> titleContains,
                                              Pageable pageable) {

        Specification<BookEntity> spec = Specification.where(null);

        if (status.isPresent()) {
            spec = spec.and(BookSpecifications.statusEquals(status.get()));
        }

        if (titleContains.isPresent() && !titleContains.get().isBlank()) {
            spec = spec.and(BookSpecifications.titleContainsIgnoreCase(titleContains.get()));
        }

        Page<BookEntity> page = bookRepository.findAll(spec, pageable);

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
    private BookResponse toResponse(BookEntity b) {
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getStatus().name(),
                b.getCreatedAt(),
                b.getUpdatedAt(),
                b.getVersion()
        );
    }
}
