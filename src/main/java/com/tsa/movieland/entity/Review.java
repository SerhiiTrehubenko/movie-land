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
@IdClass(Review.PrimaryKey.class)
public class Review {
    @Id
    @Column(name = "movie_id")
    private int movieId;
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "movie_comment")
    private String text;
    @Column(name = "review_record_time")
    private Timestamp recordTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            insertable = false,
            updatable = false
    )
    private UserDto user;

    public static class PrimaryKey implements Serializable {
        private int movieId;
        private int userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Review.PrimaryKey that = (Review.PrimaryKey) o;
            return movieId == that.movieId && userId == that.userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, userId);
        }
    }
}
