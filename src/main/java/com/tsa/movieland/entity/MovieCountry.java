package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",
            insertable = false,
            updatable = false,
            nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id",
            insertable = false,
            updatable = false,
            nullable = false)
    private Country country;

    public static MovieCountryBuilder builder() {
        return new MovieCountryBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieCountry that = (MovieCountry) o;
        return movieId == that.movieId && countryId == that.countryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, countryId);
    }

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

    public static class MovieCountryBuilder {
        private int movieId;
        private int countryId;
        private Movie movie;
        private Country country;

        MovieCountryBuilder() {
        }

        public MovieCountryBuilder movieId(int movieId) {
            this.movieId = movieId;
            return this;
        }

        public MovieCountryBuilder countryId(int countryId) {
            this.countryId = countryId;
            return this;
        }

        public MovieCountryBuilder movie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public MovieCountryBuilder country(Country country) {
            this.country = country;
            return this;
        }

        public MovieCountry build() {
            return new MovieCountry(movieId, countryId, movie, country);
        }

        public String toString() {
            return "MovieCountry.MovieCountryBuilder(movieId=" + this.movieId + ", countryId=" + this.countryId + ", movie=" + this.movie + ", country=" + this.country + ")";
        }
    }
}
