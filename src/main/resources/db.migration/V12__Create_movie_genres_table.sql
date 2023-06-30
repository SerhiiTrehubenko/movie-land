CREATE TABLE IF NOT EXISTS movie_genres
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    genre_id INTEGER NOT NULL,
    CONSTRAINT fk_genre
        FOREIGN KEY (genre_id)
            REFERENCES genres (genre_id)
);