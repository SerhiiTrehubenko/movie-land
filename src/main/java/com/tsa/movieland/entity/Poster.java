package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@Table(name = "posters")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Poster.PrimaryKey.class)
public class Poster {

    @Id
    @Column(name = "movie_id")
    private int movieId;
    @Id
    @Column(name = "poster_link")
    private String link;
    @Column(name = "poster_record_time")
    private Timestamp recordTime;

    public static class PrimaryKey implements Serializable {
        private int movieId;
        private String link;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Poster.PrimaryKey that = (Poster.PrimaryKey) o;
            return movieId == that.movieId && link == that.link;
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, link);
        }
    }
}
