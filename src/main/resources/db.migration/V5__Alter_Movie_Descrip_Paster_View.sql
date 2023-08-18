DROP VIEW IF EXISTS movies_with_description_posters;

CREATE VIEW movies_with_description_posters AS
SELECT m.movie_id, movie_rus_name, movie_native_name,
       movie_release_year,
       movie_description,
       AVG(r.movie_raring)::numeric(4,2) AS movie_rating,
       movie_price, ARRAY_AGG(po.poster_link) AS movie_posters
FROM movies m
         FULL OUTER JOIN movies_ratings r
              ON m.movie_id = r.movie_id
         FULL OUTER JOIN posters po
              ON m.movie_id = po.movie_id
GROUP BY m.movie_id
ORDER BY m.movie_id;

