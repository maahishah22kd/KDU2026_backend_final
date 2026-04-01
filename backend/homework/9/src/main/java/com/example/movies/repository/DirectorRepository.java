package com.example.movies.repository;

import com.example.movies.entity.Director;
import com.example.movies.service.DirectorService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

}
