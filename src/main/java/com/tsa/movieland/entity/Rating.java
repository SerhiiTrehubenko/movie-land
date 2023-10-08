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
public class Rating {

    @EmbeddedId
    private RatingId ratingId;
    @Column(name = "movie_raring")
    private Double rating;

    @Embeddable
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingId implements Serializable {
        @Column(name = "movie_id")
        private Integer movieId;
        @Column(name = "user_id")
        private Integer userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RatingId that = (RatingId) o;
            return Objects.equals(movieId, that.movieId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, userId);
        }
    }
}
