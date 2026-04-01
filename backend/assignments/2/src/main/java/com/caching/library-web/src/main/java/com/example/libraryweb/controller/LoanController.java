package com.example.libraryweb.controller;

import com.example.libraryapi.dto.response.LoanResponse;
import com.example.libraryservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    @Operation(
            summary = "Borrow a book (AVAILABLE -> CHECKED_OUT, creates active Loan)",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("/{bookId}/borrow")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse borrow(@PathVariable("bookId") UUID bookId, Authentication auth) {
        return loanService.borrow(bookId, auth.getName());
    }

    @PostMapping("/{bookId}/return")
    public LoanResponse returnBook(@PathVariable("bookId") UUID bookId, Authentication auth) {
        return loanService.returnBook(bookId, auth.getName());
    }
}
