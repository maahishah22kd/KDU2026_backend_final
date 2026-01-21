package com.example.movies.service;

import com.example.movies.entity.Director;
import com.example.movies.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    public Optional<Director> findDirectorById(Long id){
        return directorRepository.findById(id);
    }
}
