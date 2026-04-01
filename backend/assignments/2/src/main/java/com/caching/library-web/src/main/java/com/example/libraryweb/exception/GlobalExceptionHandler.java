package com.example.libraryweb.exception;

import com.example.libraryapi.error.ApiErrorDetail;
import com.example.libraryapi.error.ApiErrorResponse;
import com.example.libraryservice.exception.*;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArg(IllegalArgumentException ex,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(base(request, "VALIDATION_ERROR", ex.getMessage(), List.of()));
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleBookNotAvailable(BookNotAvailableException ex,
                                                                   HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(request, "BOOK_NOT_AVAILABLE", ex.getMessage(), List.of()));
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ApiErrorResponse> handleConcurrent(ConcurrentModificationException ex,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(request, "CONCURRENT_MODIFICATION", ex.getMessage(), List.of()));
    }


    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidState(InvalidStateTransitionException ex,
                                                               HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(request, "INVALID_STATE_TRANSITION", ex.getMessage(), List.of()));
    }

    // 400 - body validation (DTO annotations like @NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        List<ApiErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(base(request, "VALIDATION_ERROR", "Invalid request", details));
    }

    // 400 - malformed/missing JSON body (very common for PATCH/POST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableJson(HttpMessageNotReadableException ex,
                                                                 HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(base(request, "VALIDATION_ERROR", "Malformed or missing JSON request body", List.of()));
    }

    // 400 - invalid path/query parameter type (e.g., bad UUID in /books/{id})
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest request) {
        String field = ex.getName(); // usually "id"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(base(request, "VALIDATION_ERROR", "Invalid request parameter",
                        List.of(new ApiErrorDetail(field, "invalid value"))));
    }

    // 400 - missing required request values (rare but good coverage)
    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingRequestValue(MissingRequestValueException ex,
                                                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(base(request, "VALIDATION_ERROR", "Missing required request value", List.of()));
    }

    // 404 - resource not found (thrown from service)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex,
                                                           HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(base(request, "NOT_FOUND", ex.getMessage(), List.of()));
    }

    // 409 - illegal state transition / conflict (thrown from service)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex,
                                                           HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(request, "CONFLICT", ex.getMessage(), List.of()));
    }

    // 409 - optimistic locking conflicts (JPA/Spring)
    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(Exception ex,
                                                                 HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(request, "OPTIMISTIC_LOCK_CONFLICT",
                        "Concurrency conflict. Please retry.", List.of()));
    }

    // 500 - unexpected
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex,
                                                          HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(base(request, "INTERNAL_ERROR", "Unexpected error", List.of()));
    }

    private ApiErrorDetail toDetail(FieldError fe) {
        return new ApiErrorDetail(fe.getField(), fe.getDefaultMessage());
    }

    private ApiErrorResponse base(HttpServletRequest request,
                                  String errorCode,
                                  String message,
                                  List<ApiErrorDetail> details) {

        String correlationId = (String) request.getAttribute("correlationId");
        if (correlationId == null) correlationId = UUID.randomUUID().toString();

        return new ApiErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                errorCode,
                message,
                details,
                correlationId
        );
    }
}
