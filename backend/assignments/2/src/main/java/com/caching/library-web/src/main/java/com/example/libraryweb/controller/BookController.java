package com.example.libraryweb.controller;

import com.example.libraryapi.dto.request.CreateBookRequest;
import com.example.libraryapi.dto.request.UpdateCatalogRequest;
import com.example.libraryapi.dto.response.BookResponse;
import com.example.libraryapi.dto.response.PagedResponse;
import com.example.librarydomain.entity.BookStatus;
import com.example.libraryservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Phase 4:
     * Create book in PROCESSING.
     */
    @Operation(
            summary = "Create a book (starts in PROCESSING)",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.create(request);
    }

    /**
     * Phase 7:
     * GET /books supports:
     * - optional status
     * - optional titleContains
     * - page, size
     * - sort=createdAt,desc (Spring standard)
     */
    @GetMapping
    public PagedResponse<BookResponse> listBooks(
            @RequestParam(required = false) BookStatus status,
            @RequestParam(required = false) String titleContains,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        return bookService.search(Optional.ofNullable(status),
                Optional.ofNullable(titleContains),
                pageable);
    }

    /**
     * Phase 4:
     * PATCH transitions PROCESSING -> AVAILABLE
     */
    @Operation(
            summary = "Catalog a book (PROCESSING -> AVAILABLE)",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PatchMapping("/{id}/catalog")
    public BookResponse updateCatalog(@PathVariable("id") UUID id,
                                      @Valid @RequestBody UpdateCatalogRequest request) {
        return bookService.updateCatalog(id, request);
    }
}
