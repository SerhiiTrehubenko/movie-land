CREATE TABLE IF NOT EXISTS posters
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    poster_link VARCHAR(400) NOT NULL UNIQUE,
    poster_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);