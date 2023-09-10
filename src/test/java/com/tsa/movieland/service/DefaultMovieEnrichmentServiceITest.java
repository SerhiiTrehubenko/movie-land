package com.tsa.movieland.service;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dao.jdbc.JdbcCountryDao;
import com.tsa.movieland.dao.jdbc.JdbcMovieDao;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.UserDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.exception.MovieEnrichmentException;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev-threads")
class DefaultMovieEnrichmentServiceITest extends CommonContainer {
    private final int movieId = 1112;
    private final String expectedDescription = "Молодые влюбленные Джек и Роза находят друг друга в первом и последнем плавании «непотопляемого» Титаника. Они не могли знать, что шикарный лайнер столкнется с айсбергом в холодных водах Северной Атлантики, и их страстная любовь превратится в схватку со смертью…";
    private final List<String> expectedPicturePath = List.of(" https://images-na.ssl-images-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1._SY209_CR0,0,140,209_.jpg");
    private final List<Country> expectedCountries = List.of(Country.builder().id(501).name("США").build());
    private final List<Genre> expectedGenres = List.of(Genre.builder().id(1).name("драма").build(),
            Genre.builder().id(5).name("мелодрама").build());
    private final List<Review> expectedReviews = List.of(Review.builder().user(UserDto.builder().id(1000010).nickname("tommy").build())
            .text("В итоге мы имеем отличный представитель своего жанра, который прошёл проверку временем и до сих пор отлично смотрится, несмотря на классический сюжет").build());

    @Autowired
    private DefaultMovieEnrichmentService service;

    @Autowired
    private JdbcMovieDao movieDao;

    @SpyBean
    private JdbcCountryDao countryDao;

    @Test
    void shouldReturnFullyEnrichedMovie() {

        MovieByIdDto movieByIdDto = service.enrich(movieId, () -> movieDao.findById(movieId));

        assertNotNull(movieByIdDto);
        assertEquals(movieId, movieByIdDto.getId());
        assertEquals("Титаник", movieByIdDto.getNameRussian());
        assertEquals("Titanic", movieByIdDto.getNameNative());
        assertEquals(1997, movieByIdDto.getYearOfRelease());
        assertEquals(expectedDescription, movieByIdDto.getDescription());
        assertEquals(7.9, movieByIdDto.getRating());
        assertEquals(150.00, movieByIdDto.getPrice());
        assertEquals(expectedPicturePath, movieByIdDto.getPicturePath());
        assertIterableEquals(expectedCountries, movieByIdDto.getCountries());
        assertIterableEquals(expectedGenres, movieByIdDto.getGenres());
        assertIterableEquals(expectedReviews, movieByIdDto.getReviews());
    }

    @Test
    void shouldThrowExceptionWhenMovieFetchingTakesLongerThenFiveSeconds() {
        assertThrows(MovieEnrichmentException.class,
                () -> service
                        .enrich(movieId, () -> {
                            sleep(6000);
                            return movieDao.findById(movieId);
                        }));
    }

    @Test
    void shouldReturnPartiallyFilledMovieWhenTaskOfFetchingContentLongerFiveSecond() {
        when(countryDao.findByMovieId(movieId)).thenAnswer((Answer<Iterable<Country>>) invoke -> getCountriesWithTimeOut());

        MovieByIdDto movieByIdDto = service.enrich(movieId, () -> movieDao.findById(movieId));

        assertNotNull(movieByIdDto);
        assertEquals(movieId, movieByIdDto.getId());
        assertEquals("Титаник", movieByIdDto.getNameRussian());
        assertEquals("Titanic", movieByIdDto.getNameNative());
        assertEquals(1997, movieByIdDto.getYearOfRelease());
        assertEquals(expectedDescription, movieByIdDto.getDescription());
        assertEquals(7.9, movieByIdDto.getRating());
        assertEquals(150.00, movieByIdDto.getPrice());
        assertEquals(expectedPicturePath, movieByIdDto.getPicturePath());
        assertIterableEquals(List.of(), movieByIdDto.getCountries());
        assertIterableEquals(expectedGenres, movieByIdDto.getGenres());
        assertIterableEquals(expectedReviews, movieByIdDto.getReviews());
    }

    private Iterable<Country> getCountriesWithTimeOut() {
        sleep(6000);
        return countryDao.findByMovieId(movieId);
    }

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}