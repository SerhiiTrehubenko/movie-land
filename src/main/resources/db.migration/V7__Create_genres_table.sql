CREATE SEQUENCE IF NOT EXISTS genres_id
    AS INT
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 500
    START 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS genres (
  genre_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('genres_id') CHECK (genre_id BETWEEN 1 AND 500),
  genre_name VARCHAR(100) NOT NULL UNIQUE
);

ALTER SEQUENCE genres_id
    OWNED BY genres.genre_id;

CREATE FUNCTION  forbid_update_genre_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.genre_id != OLD.genre_id OR
       NEW.genre_id < 1 OR
       NEW.genre_id > 500 THEN
        RAISE EXCEPTION 'update forbidden';
    END IF;
END;
$$;

CREATE TRIGGER genre_id_update
    BEFORE UPDATE
    ON genres
    FOR EACH ROW
EXECUTE PROCEDURE forbid_update_genre_id();