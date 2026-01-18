package com.example.librarydomain.repo;

import com.example.librarydomain.entity.BookEntity;
import com.example.librarydomain.entity.BookStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 * Reusable JPA Specifications for dynamic book searching.
 */
public final class BookSpecifications {

    private BookSpecifications() {}

    public static Specification<BookEntity> statusEquals(BookStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<BookEntity> titleContainsIgnoreCase(String titleContains) {
        String pattern = "%" + titleContains.toLowerCase().trim() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern);
    }
}
