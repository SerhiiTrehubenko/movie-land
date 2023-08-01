package com.tsa.movieland.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
@DataSet(value = {
        "datasets/users/dataset-users.json",
        "datasets/genres/dataset-genres.json",
        "datasets/movies/dataset-movies.json",
        "datasets/countries/dataset-countries.json",
        "datasets/jointable/dataset-movies_genres.json",
        "datasets/jointable/dataset-movies_ratings.json",
        "datasets/jointable/dataset-movies_countries.json",
        "datasets/jointable/dataset-movie_reviews.json",
        "datasets/posters/dataset-posters.json"
},
        cleanAfter = true, cleanBefore = true,
        skipCleaningFor = "flyway_schema_history"
)
public abstract class DaoBaseTest {

}
