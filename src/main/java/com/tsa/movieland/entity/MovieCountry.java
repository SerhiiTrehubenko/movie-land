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
@Table(name = "movies_countries")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MovieCountry.PrimaryKey.class)
public class MovieCountry {

    @Id
    @Column(name = "movie_id")
    private int movieId;
    @Id
    @Column(name = "country_id")
    private int countryId;

    public static class PrimaryKey implements Serializable {
        private int movieId;
        private int countryId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return movieId == that.movieId && countryId == that.countryId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, countryId);
        }
    }
}
