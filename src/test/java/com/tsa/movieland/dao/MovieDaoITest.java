package com.tsa.movieland.dao;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovieDaoITest extends CommonContainer {

    @Autowired
    MovieDao movieDao;
    @Autowired
    CountryDao countryDao;
    @Autowired
    GenreDao genreDao;

    @Test
    void shouldReturnListOfMovies() {
        Iterable<MovieFindAllDto> movies = movieDao.findAll();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(25, numberMovies);
    }

    @Test
    void shouldReturnThreeRandomMovies() {
        Iterable<MovieFindAllDto> movies = movieDao.findRandom();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(3, numberMovies);
    }

    @Test
    void shouldReturnMoviesByGenre() {
        Iterable<MovieFindAllDto> movies = movieDao.findByGenreId(2);

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

    @Test
    void shouldInsertNewMovie() {
        int nextIdInMoviesTable = 1126;
        String value = "test";
        final AddUpdateMovieDto addUpdateMovieDto = AddUpdateMovieDto.builder()
                .nameRussian(value)
                .nameNative(value)
                .yearOfRelease(2023)
                .description(value)
                .price(256.78)
                .countries(List.of(501,502))
                .genres(List.of(1, 2))
                .build();
        final int movieId = movieDao.save(addUpdateMovieDto);
        assertEquals(nextIdInMoviesTable, movieId);

        final MovieByIdDto movie = movieDao.findById(nextIdInMoviesTable);
        assertNotNull(movie);

        assertEquals(value, movie.getNameRussian());
        assertEquals(value, movie.getNameNative());
        assertEquals(2023, movie.getYearOfRelease());
        assertEquals(value, movie.getDescription());
        assertEquals(256.78, movie.getPrice());

        final List<Country> countries = (List<Country>) countryDao.findByMovieId(movieId);
        assertEquals(501, countries.get(0).getId());
        assertEquals("США", countries.get(0).getName());
        assertEquals(502, countries.get(1).getId());
        assertEquals("Франция", countries.get(1).getName());

        final List<Genre> genres = (List<Genre>) genreDao.findByMovieId(movieId);
        assertEquals(1, genres.get(0).getId());
        assertEquals("драма", genres.get(0).getName());
        assertEquals(2, genres.get(1).getId());
        assertEquals("криминал", genres.get(1).getName());
    }

    @Test
    void shouldUpdateExistedMovie() {
        String rusNameBeforeUpdate = "Большой куш";
        String nativeNameBeforeUpdate = "Snatch.";
        int yearBeforeUpdate = 2000;
        String descriptionBeforeUpdate = "Четырехпалый Френки должен был переправить краденый алмаз из Англии в США своему боссу Эви. Но вместо этого герой попадает в эпицентр больших неприятностей. Сделав ставку на подпольном боксерском поединке, Френки попадает в круговорот весьма нежелательных событий. Вокруг героя и его груза разворачивается сложная интрига с участием множества колоритных персонажей лондонского дна — русского гангстера, троих незадачливых грабителей, хитрого боксера и угрюмого громилы грозного мафиози. Каждый норовит в одиночку сорвать Большой Куш.";
        double priceBeforeUpdate = 160.00;

        String valueTest = "test";
        int idExistMovie = 1116;
        List<Integer> countriesToUpdate = List.of(505, 506);
        List<Integer> genresToUpdate = List.of(1, 2, 3);

        final MovieByIdDto foundMovieBeforeUpdate = movieDao.findById(1116);
        assertEquals(rusNameBeforeUpdate, foundMovieBeforeUpdate.getNameRussian());
        assertEquals(nativeNameBeforeUpdate, foundMovieBeforeUpdate.getNameNative());
        assertEquals(yearBeforeUpdate, foundMovieBeforeUpdate.getYearOfRelease());
        assertEquals(descriptionBeforeUpdate, foundMovieBeforeUpdate.getDescription());
        assertEquals(priceBeforeUpdate, foundMovieBeforeUpdate.getPrice());

        final AddUpdateMovieDto movieWithUpdateData = AddUpdateMovieDto.builder()
                .nameRussian(valueTest)
                .nameNative(valueTest)
                .yearOfRelease(2023)
                .description(valueTest)
                .price(256.78)
                .countries(countriesToUpdate)
                .genres(genresToUpdate)
                .build();

        movieDao.update(idExistMovie, movieWithUpdateData);

        final MovieByIdDto updatedMovie = movieDao.findById(idExistMovie);
        assertNotNull(updatedMovie);

        assertEquals(valueTest, updatedMovie.getNameRussian());
        assertEquals(valueTest, updatedMovie.getNameNative());
        assertEquals(2023, updatedMovie.getYearOfRelease());
        assertEquals(valueTest, updatedMovie.getDescription());
        assertEquals(256.78, updatedMovie.getPrice());

        final List<Country> updatedCountries = (List<Country>) countryDao.findByMovieId(idExistMovie);
        assertEquals(countriesToUpdate.get(0), updatedCountries.get(0).getId());
        assertEquals(countriesToUpdate.get(1), updatedCountries.get(1).getId());

        final List<Genre> updatedGenres = (List<Genre>) genreDao.findByMovieId(idExistMovie);
        assertEquals(genresToUpdate.get(0), updatedGenres.get(0).getId());
        assertEquals(genresToUpdate.get(1), updatedGenres.get(1).getId());
        assertEquals(genresToUpdate.get(2), updatedGenres.get(2).getId());

    }
}

