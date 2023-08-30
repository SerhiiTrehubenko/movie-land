ALTER TABLE IF EXISTS movies_ratings
    ADD CONSTRAINT compound_id UNIQUE (movie_id, user_id);