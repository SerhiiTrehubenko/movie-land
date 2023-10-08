package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.common.AvgRating;
import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.RatingDao;
import com.tsa.movieland.entity.Rating;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@JpaDao
@RequiredArgsConstructor
public class JpaRatingDao implements RatingDao {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public Iterable<AvgRating> fidAllAvgRatingsGroupedMovie() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AvgRating> criteria = builder.createQuery(AvgRating.class);

        Root<Rating> ratingRoot = criteria.from(Rating.class);
        Path<Object> movieId = ratingRoot.get("ratingId").get("movieId");
        Path<Object> userId = ratingRoot.get("ratingId").get("userId");

        criteria.select(
                builder.construct(AvgRating.class,
                        movieId,
                        builder.avg(ratingRoot.get("rating")),
                        builder.count(userId)
                )
        );

        criteria.groupBy(movieId);
        criteria.orderBy(builder.asc(movieId));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    @Transactional
    public void saveBuffer(Set<RatingRequest> avgRatings) {
        Set<RatingRequest> intermediateRatings = new HashSet<>();
        Iterator<RatingRequest> iterator = avgRatings.iterator();
        Session hibernateSession = entityManager.unwrap(Session.class);

        String insertQuery = "INSERT INTO movies_ratings (movie_id, user_id, movie_raring) " +
                "VALUES (?, (SELECT user_id FROM users where user_email=?), ?) " +
                "ON CONFLICT ON CONSTRAINT compound_id DO UPDATE SET movie_raring = ?";

        hibernateSession.doWork(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                while (iterator.hasNext()) {
                    RatingRequest next = iterator.next();

                    preparedStatement.setInt(1, next.getMovieId());
                    preparedStatement.setString(2, next.getUserEmail());
                    preparedStatement.setDouble(3, next.getRating());
                    preparedStatement.setDouble(4, next.getRating());

                    preparedStatement.addBatch();
                    intermediateRatings.add(next);
                    iterator.remove();
                }
                preparedStatement.executeBatch();
            } catch (Exception e) {
                avgRatings.addAll(intermediateRatings);
                throw new RuntimeException(e);
            }
        });
    }
}
