CREATE SEQUENCE IF NOT EXISTS movies_id
    AS INT
    INCREMENT 1
    MINVALUE 1101
    MAXVALUE 1000000
    START 1101
    NO CYCLE;

CREATE TABLE IF NOT EXISTS movies (
    movie_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('movies_id') CHECK (movie_id BETWEEN 1101 AND 1000000),
    movie_rus_name VARCHAR(200) NOT NULL,
    movie_en_name VARCHAR(200) NOT NULL,
    movie_release_year SMALLINT NOT NULL,
    movie_description VARCHAR(800) UNIQUE NOT NULL,
    movie_price NUMERIC(8, 2) NOT NULL,
    movie_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT (TRUE)
);

ALTER SEQUENCE movies_id
OWNED BY movies.movie_id;

CREATE FUNCTION  forbid_update_movie_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.movie_id <> OLD.movie_id OR
       NEW.movie_id < 1101 OR
       NEW.movie_id > 1000000 THEN
    RAISE EXCEPTION 'update forbidden';
    END IF;
END;
$$;

CREATE TRIGGER movie_id_update
    BEFORE UPDATE
    ON movies
    FOR EACH ROW
    EXECUTE PROCEDURE forbid_update_movie_id();

