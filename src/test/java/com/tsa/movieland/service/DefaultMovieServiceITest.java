package com.tsa.movieland.service;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.entity.MovieCountry;
import com.tsa.movieland.entity.MovieGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("dev-async")
public class DefaultMovieServiceITest extends CommonContainer {

    @Autowired
    MovieDao movieDao;

    @Autowired
    MovieService movieService;

    @Test
    public void shouldReturnListOfMovies() {
        final MovieRequest emptyMovieRequest = MovieRequest.EMPTY_MOVIE_REQUEST;
        Iterable<MovieFindAllDto> movies = movieService.findAll(emptyMovieRequest);

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(25, numberMovies);
    }

    @Test
    public void shouldReturnThreeRandomMovies() {
        List<MovieFindAllDto> movies = (List<MovieFindAllDto>) movieService.findRandom();

        assertNotNull(movies);

        assertEquals(3, movies.size());
    }

    @Test
    public void shouldReturnMoviesByGenre() {

        List<MovieFindAllDto> movies = (List<MovieFindAllDto>) movieService.findByGenre(2, MovieRequest.EMPTY_MOVIE_REQUEST);

        assertNotNull(movies);

        assertEquals(7, movies.size());
    }

    @Test
    public void shouldReturnMovieByIdWithCountriesGenresReviews() {
        String description = "В разгар гражданской войны таинственный стрелок скитается по просторам Дикого Запада. " +
                "У него нет ни дома, ни друзей, ни компаньонов, пока он не встречает двоих незнакомцев, таких же " +
                "безжалостных и циничных. По воле судьбы трое мужчин вынуждены объединить свои усилия в поисках " +
                "украденного золота. Но совместная работа — не самое подходящее занятие для таких отъявленных бандитов, " +
                "как они. Компаньоны вскоре понимают, что в их дерзком и опасном путешествии по разоренной войной стране " +
                "самое важное — никому не доверять и держать пистолет наготове, если хочешь остаться в живых.";
        String picturePath = " https://images-na.ssl-images-amazon.com/images/M/MV5BOTQ5NDI3MTI4MF5BMl5BanBnXkFtZTgwNDQ4ODE5MDE@._V1._SX140_CR0,0,140,209_.jpg";

        MovieByIdDto movie = movieService.getById(1121, MovieRequest.EMPTY_MOVIE_REQUEST);

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
    public void shouldInsertNewMovie() {
        int nextIdInMoviesTable = 1126;
        String rusName = "rusName";
        String nativeName = "nativeName";
        String description = "description";
        List<String> picturePath = List.of("IMAGE");
        final AddUpdateMovieDto addUpdateMovieDto = AddUpdateMovieDto.builder()
                .nameRussian(rusName)
                .nameNative(nativeName)
                .yearOfRelease(2023)
                .description(description)
                .picturePath("IMAGE")
                .price(256.78)
                .countries(List.of(501,502))
                .genres(List.of(1, 2))
                .build();
        int movieId = movieService.save(addUpdateMovieDto);
        assertEquals(nextIdInMoviesTable, movieId);

        Movie savedMovie = movieDao.findByIdForUpdate(movieId);
        assertNotNull(savedMovie);

        assertEquals(rusName, savedMovie.getNameRussian());
        assertEquals(nativeName, savedMovie.getNameNative());
        assertEquals(2023, savedMovie.getYearOfRelease());
        assertEquals(description, savedMovie.getDescription());
        assertEquals(picturePath, savedMovie.getPosters());
        assertEquals(256.78, savedMovie.getPrice());

        final List<MovieCountry> countries = savedMovie.getMovieCountries();
        assertEquals(501, countries.get(0).getCountry().getId());
        assertEquals("США", countries.get(0).getCountry().getName());
        assertEquals(502, countries.get(1).getCountry().getId());
        assertEquals("Франция", countries.get(1).getCountry().getName());

        final List<MovieGenre> genres = savedMovie.getMovieGenres();
        assertEquals(1, genres.get(0).getGenre().getId());
        assertEquals("драма", genres.get(0).getGenre().getName());
        assertEquals(2, genres.get(1).getGenre().getId());
        assertEquals("криминал", genres.get(1).getGenre().getName());
    }

    @Test
    public void shouldUpdateExistedMovie() {
        String rusNameBeforeUpdate = "Большой куш";
        String nativeNameBeforeUpdate = "Snatch.";
        int yearBeforeUpdate = 2000;
        String descriptionBeforeUpdate = "Четырехпалый Френки должен был переправить краденый алмаз из Англии в США своему боссу Эви. Но вместо этого герой попадает в эпицентр больших неприятностей. Сделав ставку на подпольном боксерском поединке, Френки попадает в круговорот весьма нежелательных событий. Вокруг героя и его груза разворачивается сложная интрига с участием множества колоритных персонажей лондонского дна — русского гангстера, троих незадачливых грабителей, хитрого боксера и угрюмого громилы грозного мафиози. Каждый норовит в одиночку сорвать Большой Куш.";
        double priceBeforeUpdate = 160.00;

        String rusNameNew = "rusName";
        String nativeNameNew = "nativeName";
        String descriptionNew = "description";
        String posterUpdateLink = "poster TEST; 0";
        String posterNew = "poster TEST";
        int idExistMovie = 1116;
        List<Integer> countriesToUpdate = List.of(505, 506);
        List<Integer> genresToUpdate = List.of(1, 2, 3);

        Movie foundMovieBeforeUpdate = movieDao.findById(1116);
        assertEquals(rusNameBeforeUpdate, foundMovieBeforeUpdate.getNameRussian());
        assertEquals(nativeNameBeforeUpdate, foundMovieBeforeUpdate.getNameNative());
        assertEquals(yearBeforeUpdate, foundMovieBeforeUpdate.getYearOfRelease());
        assertEquals(descriptionBeforeUpdate, foundMovieBeforeUpdate.getDescription());
        assertEquals(priceBeforeUpdate, foundMovieBeforeUpdate.getPrice());

        AddUpdateMovieDto movieWithUpdateData = AddUpdateMovieDto.builder()
                .nameRussian(rusNameNew)
                .nameNative(nativeNameNew)
                .yearOfRelease(2023)
                .description(descriptionNew)
                .price(256.78)
                .countries(countriesToUpdate)
                .genres(genresToUpdate)
                .picturePath(posterUpdateLink)
                .build();

        movieService.update(idExistMovie, movieWithUpdateData);

        Movie updatedMovie = movieDao.findByIdForUpdate(idExistMovie);
        assertNotNull(updatedMovie);

        assertEquals(rusNameNew, updatedMovie.getNameRussian());
        assertEquals(nativeNameNew, updatedMovie.getNameNative());
        assertEquals(2023, updatedMovie.getYearOfRelease());
        assertEquals(descriptionNew, updatedMovie.getDescription());
        assertEquals(256.78, updatedMovie.getPrice());
        assertEquals(posterNew, updatedMovie.getPosters().get(0));

        List<MovieCountry> updatedCountries = updatedMovie.getMovieCountries();
        assertEquals(countriesToUpdate.get(0), updatedCountries.get(0).getCountry().getId());
        assertEquals(countriesToUpdate.get(1), updatedCountries.get(1).getCountry().getId());

        List<MovieGenre> updatedGenres = updatedMovie.getMovieGenres();
        assertEquals(genresToUpdate.get(0), updatedGenres.get(0).getGenre().getId());
        assertEquals(genresToUpdate.get(1), updatedGenres.get(1).getGenre().getId());
        assertEquals(genresToUpdate.get(2), updatedGenres.get(2).getGenre().getId());
    }
}