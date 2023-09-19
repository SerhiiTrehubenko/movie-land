package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {
    @Id
    @SequenceGenerator(
            name = "countries_id",
            sequenceName = "countries_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "countries_id"
    )
    @Column(name = "country_id")
    private int id;
    @Column(name = "country_name")
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "movies_countries",
            joinColumns = @JoinColumn(name = "country_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<MovieEntity> movies;
}
