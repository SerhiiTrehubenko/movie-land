CREATE TABLE IF NOT EXISTS movie_ratings
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    movie_raring NUMERIC(4, 2) NOT NULL CHECK (movie_raring BETWEEN 0 AND 10),
    rating_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);