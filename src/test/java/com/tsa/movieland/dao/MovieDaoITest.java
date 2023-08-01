package com.tsa.movieland.dao;

import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovieDaoITest extends DaoBaseTest {

    @Autowired
    MovieDao movieDao;

    @Test
    void shouldReturnListOfMovies() {
        Iterable<Movie> movies = movieDao.findAll();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(25, numberMovies);
    }

    @Test
    void shouldReturnThreeRandomMovies() {
        Iterable<Movie> movies = movieDao.findRandom();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(3, numberMovies);
    }

    @Test
    void shouldReturnMoviesByGenre() {
        Iterable<Movie> movies = movieDao.findByGenreId(2);

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(7, numberMovies);
    }

    @Test
    void shouldReturnMovieByIdWithCountriesGenresReviews() {
        String description = "В разгар гражданской войны таинственный стрелок скитается по просторам Дикого Запада. " +
                "У него нет ни дома, ни друзей, ни компаньонов, пока он не встречает двоих незнакомцев, таких же " +
                "безжалостных и циничных. По воле судьбы трое мужчин вынуждены объединить свои усилия в поисках " +
                "украденного золота. Но совместная работа — не самое подходящее занятие для таких отъявленных бандитов, " +
                "как они. Компаньоны вскоре понимают, что в их дерзком и опасном путешествии по разоренной войной стране " +
                "самое важное — никому не доверять и держать пистолет наготове, если хочешь остаться в живых.";
        String picturePath = " https://images-na.ssl-images-amazon.com/images/M/MV5BOTQ5NDI3MTI4MF5BMl5BanBnXkFtZTgwNDQ4ODE5MDE@._V1._SX140_CR0,0,140,209_.jpg";

        MovieByIdDto movie = movieDao.findById(1121);

        assertNotNull(movie);
        assertEquals(1121, movie.getId());
        assertEquals("Хороший, плохой, злой", movie.getNameRussian());
        assertEquals("Il buono, il brutto, il cattivo", movie.getNameNative());
        assertEquals("Il buono, il brutto, il cattivo", movie.getNameNative());
        assertEquals(description, movie.getDescription());
        assertEquals(8.5, movie.getRating());
        assertEquals(130.0, movie.getPrice());
        assertEquals(picturePath, movie.getPicturePath().get(0));

    }
}

