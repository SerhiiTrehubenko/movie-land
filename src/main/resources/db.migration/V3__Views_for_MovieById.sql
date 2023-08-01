CREATE VIEW movies_with_description_posters AS
SELECT m.movie_id, movie_rus_name, movie_native_name,
       movie_release_year,
       movie_description,
       AVG(r.movie_raring)::numeric(4,2) AS movie_rating,
       movie_price, ARRAY_AGG(po.poster_link) AS movie_posters
FROM movies m
         JOIN movies_ratings r
              ON m.movie_id = r.movie_id
         JOIN posters po
              ON m.movie_id = po.movie_id
GROUP BY m.movie_id
ORDER BY m.movie_id;

CREATE VIEW countries_by_movie_id AS
    SELECT mc.movie_id, c.country_id, c.country_name
    FROM countries c, movies_countries mc
    WHERE mc.country_id = c.country_id
    ORDER BY c.country_id;

CREATE VIEW genres_by_movie_id AS
    SELECT mg.movie_id, g.genre_id, g.genre_name
    FROM genres g, movies_genres mg
    WHERE g.genre_id = mg.genre_id
    ORDER BY g.genre_id;

CREATE VIEW review_with_user_by_movie_id AS
    SELECT mr.movie_id, u.user_id, u.user_nickname, mr.movie_comment
    FROM movie_reviews mr, users u
    WHERE mr.user_id = u.user_id
    ORDER BY u.user_id;

