package com.example.movies.controller;
import com.example.movies.entity.Director;
import com.example.movies.entity.Movie;
import com.example.movies.entity.Review;
import com.example.movies.repository.DirectorRepository;
import com.example.movies.repository.MovieRepository;
import com.example.movies.repository.ReviewRepository;
import com.example.movies.service.DirectorService;
import com.example.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final DirectorService directorService;
    private final ReviewRepository reviewRepository;

    @QueryMapping
    public Movie findMovieById(@Argument Long id) {
        return movieService.findMovieById(id).orElse(null);
    }

    @SchemaMapping(typeName = "Movie", field = "director")
    public Director director(Movie movie){
        return directorService.findDirectorById(movie.getDirectorId()).orElse(null);
    }

    @SchemaMapping(typeName = "Movie", field = "reviews")
    public List<Review> reviews(Movie movie) {
        return reviewRepository.findByMovieId(movie.getId());
    }


    @MutationMapping
    public Movie addReview(
            @Argument Long movieId,
            @Argument String comment,
            @Argument Integer rating
    ) {
        Movie movie = movieService.findMovieById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setMovie(movie);

        reviewRepository.save(review);
        return movie;
    }

}
