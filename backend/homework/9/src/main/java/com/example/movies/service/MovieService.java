package com.example.movies.service;
import com.example.movies.entity.Movie;
import com.example.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public Optional<Movie> findMovieById(Long id){
        return movieRepository.findById(id);
    }
}
