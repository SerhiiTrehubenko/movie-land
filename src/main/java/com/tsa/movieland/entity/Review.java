package com.tsa.movieland.entity;

import com.tsa.movieland.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@Table(name = "movie_reviews")
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @EmbeddedId
    private ReviewId reviewId;

    @Column(name = "movie_comment")
    private String text;
    @Column(name = "review_record_time")
    private Timestamp recordTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            insertable = false,
            updatable = false,
            nullable = false
    )
    private UserDto user;

    @Embeddable
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewId implements Serializable {

        @Column(name = "movie_id")
        private int movieId;

        @Column(name = "user_id")
        private int userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReviewId reviewId = (ReviewId) o;
            return movieId == reviewId.movieId && userId == reviewId.userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, userId);
        }
    }
}

