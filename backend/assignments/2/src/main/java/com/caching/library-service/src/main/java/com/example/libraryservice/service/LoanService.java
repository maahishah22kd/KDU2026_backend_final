package com.example.libraryservice.service;

import com.example.libraryapi.dto.response.LoanResponse;
import com.example.librarydomain.entity.BookEntity;
import com.example.librarydomain.entity.BookStatus;
import com.example.librarydomain.entity.LoanEntity;
import com.example.librarydomain.entity.UserEntity;
import com.example.librarydomain.repo.BookRepository;
import com.example.librarydomain.repo.LoanRepository;
import com.example.librarydomain.repo.UserRepository;
import com.example.libraryservice.aop.AuditAction;
import com.example.libraryservice.exception.BookNotAvailableException;
import com.example.libraryservice.exception.ConcurrentModificationException;
import com.example.libraryservice.exception.ConflictException;
import com.example.libraryservice.exception.NotFoundException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


/**
 * Application service for borrowing and returning books.
 *
 * <p>Key invariants:</p>
 * <ul>
 *   <li>At most one active loan exists per book (active = returnedAt is null).</li>
 *   <li>A book can be borrowed only when its status is AVAILABLE.</li>
 *   <li>Returning a book closes the active loan and sets the book status back to AVAILABLE.</li>
 * </ul>
 *
 * <p>Audit:</p>
 * <ul>
 *   <li>Methods may be annotated with {@link com.example.libraryservice.aop.AuditAction}
 *       to log user action and affected bookId.</li>
 * </ul>
 */
@Service
public class LoanService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanService(BookRepository bookRepository,
                       LoanRepository loanRepository,
                       UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

/**
 * Borrows the specified book for the given username.
 */
    @AuditAction("borrow")
    @Transactional
    public LoanResponse borrow(UUID bookId, String borrowerUsername) {
        try {
            BookEntity book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

            if (book.getStatus() != BookStatus.AVAILABLE) {
                throw new BookNotAvailableException("BOOK_NOT_AVAILABLE");
            }

            UserEntity borrower = userRepository.findByUsername(borrowerUsername)
                    .orElseThrow(() -> new NotFoundException("User not found: " + borrowerUsername));

            // Update book status first (this is the write that triggers optimistic locking)
            book.setStatus(BookStatus.CHECKED_OUT);
            bookRepository.save(book);

            // Enforce “one active loan per book” at service level
            loanRepository.findByBook_IdAndReturnedAtIsNull(bookId)
                    .ifPresent(l -> { throw new BookNotAvailableException("BOOK_NOT_AVAILABLE"); });

            LoanEntity loan = loanRepository.save(new LoanEntity(book, borrower, Instant.now()));
            return toResponse(loan);

        } catch (OptimisticLockingFailureException ex) {
            // This is the required mapping path for concurrency
            throw new ConcurrentModificationException("CONCURRENT_MODIFICATION");
        }
    }


/**
 * Returns the specified book.
 */
    @AuditAction("return")
    @Transactional
    public LoanResponse returnBook(UUID bookId, String username) {
        try {
            BookEntity book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

            if (book.getStatus() != BookStatus.CHECKED_OUT) {
                throw new BookNotAvailableException("No active checkout for this book");
            }

            LoanEntity activeLoan = loanRepository.findByBook_IdAndReturnedAtIsNull(bookId)
                    .orElseThrow(() -> new BookNotAvailableException("No active loan to return"));

            // Optional “only borrower can return”
            if (!activeLoan.getBorrower().getUsername().equals(username)) {
                throw new ConflictException("Only borrower can return");
            }

            activeLoan.markReturned(Instant.now());
            loanRepository.save(activeLoan);

            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);

            return toResponse(activeLoan);

        } catch (OptimisticLockingFailureException ex) {
            throw new ConcurrentModificationException("CONCURRENT_MODIFICATION");
        }
    }

    private LoanResponse toResponse(LoanEntity l) {
        return new LoanResponse(
                l.getId(),
                l.getBook().getId(),
                l.getBorrower().getId(),
                l.getBorrowedAt(),
                l.getReturnedAt()
        );
    }
}
