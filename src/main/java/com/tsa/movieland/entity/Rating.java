package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@Table(name = "movies_ratings")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Rating.PrimaryKey.class)
public class Rating {

    @Id
    @Column(name = "movie_id")
    private Integer movieId;
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "movie_raring")
    private Double rating;

    public static class PrimaryKey implements Serializable {
        private Integer movieId;
        private Integer userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(movieId, that.movieId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, userId);
        }
    }
}
