CREATE TABLE IF NOT EXISTS movie_reviews
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    movie_comment VARCHAR(600) NOT NULL UNIQUE,
    review_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);