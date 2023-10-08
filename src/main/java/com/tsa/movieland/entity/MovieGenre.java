package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter
@Entity
@Table(name = "movies_genres")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MovieGenre.PrimaryKey.class)
public class MovieGenre {

    @Id
    @Column(name = "movie_id")
    private int movieId;
    @Id
    @Column(name = "genre_id")
    private int genreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", insertable = false, updatable = false)
    private Genre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGenre that = (MovieGenre) o;
        return movieId == that.movieId && genreId == that.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, genreId);
    }

    public static class PrimaryKey implements Serializable {
        private int movieId;
        private int genreId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return movieId == that.movieId && genreId == that.genreId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, genreId);
        }
    }
}
