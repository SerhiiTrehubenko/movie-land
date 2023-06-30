CREATE TABLE IF NOT EXISTS movie_countries
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    country_id INTEGER NOT NULL,
    CONSTRAINT fk_country
        FOREIGN KEY (country_id)
            REFERENCES countries (country_id)
);