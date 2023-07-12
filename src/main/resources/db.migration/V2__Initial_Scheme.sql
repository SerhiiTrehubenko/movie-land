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
    movie_native_name VARCHAR(200) NOT NULL,
    movie_release_year SMALLINT NOT NULL,
    movie_description VARCHAR(800) UNIQUE NOT NULL,
    movie_price NUMERIC(8, 2) NOT NULL,
    movie_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT (TRUE)
);

ALTER SEQUENCE movies_id
    OWNED BY movies.movie_id;

CREATE SEQUENCE IF NOT EXISTS roles_id
    AS INT
    INCREMENT 1
    MINVALUE 1001
    MAXVALUE 1100
    START 1001
    NO CYCLE;

CREATE TABLE IF NOT EXISTS roles (
    role_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('roles_id') CHECK (role_id BETWEEN 1001 AND 1100),
    role_name VARCHAR(50) NOT NULL UNIQUE
);

ALTER SEQUENCE roles_id
    OWNED BY roles.role_id;

CREATE SEQUENCE IF NOT EXISTS users_id
    AS INT
    INCREMENT 1
    MINVALUE 1000001
    START 1000001
    NO CYCLE;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('users_id') CHECK (user_id >= 1000001),
    user_firstname VARCHAR(100) NOT NULL,
    user_lastname VARCHAR(100) NOT NULL,
    user_nickname VARCHAR(100) NOT NULL UNIQUE,
    user_email VARCHAR(100) NOT NULL UNIQUE,
    user_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER SEQUENCE users_id
    OWNED BY users.user_id;

CREATE FUNCTION  get_pk_role_id()
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
BEGIN
    SELECT role_id FROM roles WHERE role_name LIKE 'USER';
END;
$$;

CREATE TABLE IF NOT EXISTS user_credentials (
    user_id INTEGER UNIQUE NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users(user_id),
    role_id INTEGER NOT NULL DEFAULT (get_pk_role_id()),
    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
            REFERENCES roles(role_id),
    md5_password VARCHAR(100) NOT NULL UNIQUE,
    sole  VARCHAR(100) NOT NULL UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS countries_id
    AS INT
    INCREMENT 1
    MINVALUE 501
    MAXVALUE 1000
    START 501
    NO CYCLE;

CREATE TABLE IF NOT EXISTS countries (
    country_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('countries_id') CHECK (country_id BETWEEN 501 AND 1000),
    country_name VARCHAR(100) NOT NULL UNIQUE
);

ALTER SEQUENCE countries_id
    OWNED BY countries.country_id;

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

CREATE TABLE IF NOT EXISTS posters
(
    movie_id INTEGER NOT NULL,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id)
            REFERENCES movies (movie_id),
    poster_link VARCHAR(400) NOT NULL UNIQUE,
    poster_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

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

CREATE VIEW movies_expand AS
SELECT m.movie_id, movie_rus_name, movie_native_name,
       movie_release_year, AVG(r.movie_raring)::numeric(4,2) AS movie_rating,
       movie_price, ARRAY_AGG(po.poster_link) AS posters
FROM movies m
         JOIN movie_ratings r
              ON m.movie_id = r.movie_id
         JOIN posters po
              ON m.movie_id = po.movie_id
GROUP BY m.movie_id
ORDER BY m.movie_id;

