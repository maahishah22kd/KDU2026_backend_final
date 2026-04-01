package com.example.movies.repository;

import com.example.movies.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByMovieId(Long movieId);
}
