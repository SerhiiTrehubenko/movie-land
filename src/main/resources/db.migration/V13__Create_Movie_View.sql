CREATE VIEW movies_expand AS
SELECT m.movie_id, movie_rus_name, movie_en_name,
       movie_release_year, AVG(r.movie_raring)::numeric(4,2) AS movie_rating,
       movie_price, ARRAY_AGG(po.poster_link) AS posters
FROM movies m
         JOIN movie_ratings r
              ON m.movie_id = r.movie_id
         JOIN posters po
              ON m.movie_id = po.movie_id
GROUP BY m.movie_id
ORDER BY m.movie_id;