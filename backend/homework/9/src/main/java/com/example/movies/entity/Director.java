package com.example.movies.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_awards", nullable = false)
    private int totalAwards;
}