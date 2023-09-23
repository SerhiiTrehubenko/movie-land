package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Movie {
    @Id
    @SequenceGenerator(
            name = "movies_id",
            sequenceName = "movies_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movies_id"
    )
    @Column(name = "movie_id")
    private Integer id;
    @Column(name = "movie_rus_name")
    private String nameRussian;
    @Column(name = "movie_native_name")
    private String nameNative;
    @Column(name = "movie_release_year")
    private Integer yearOfRelease;
    @Column(name = "movie_description")
    private String description;
    @Column(name = "movie_price")
    private Double price;
    @Formula("(SELECT avg(mr.movie_raring) FROM movies_ratings mr WHERE mr.movie_id = movie_id GROUP BY movie_id)")
    private Double rating;

    @OneToMany(
            cascade = CascadeType.REMOVE, fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "movie_id",
            referencedColumnName = "movie_id",
            insertable = false,
            updatable = false
    )
    private List<Poster> posters;
}
