package com.tsa.movieland.common;

import com.tsa.movieland.entity.MovieFindAllDto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import static com.tsa.movieland.common.SortDirection.*;
public class MovieSorter {
    private final static String NULL = "null";
    private final static String RATING = SortField.RATING.name();
    private final static String PRICE = SortField.PRICE.name();
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_RATING = Comparator.comparing(MovieFindAllDto::getRating);
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_PRICE = Comparator.comparing(MovieFindAllDto::getPrice);

    private final Map<String, Consumer<List<MovieFindAllDto>>> sortMethods = Map.of(
            RATING + ASC, list -> list.sort(COMPARATOR_BY_RATING),
            RATING + DESC, list -> list.sort(COMPARATOR_BY_RATING.reversed()),
            PRICE + ASC, list -> list.sort(COMPARATOR_BY_PRICE),
            PRICE + DESC, list -> list.sort(COMPARATOR_BY_PRICE.reversed()),
            NULL + NULL, list -> {
//                "do nothing"
            }
    );

    public void sort(List<MovieFindAllDto> movieDaoAll, MovieRequest movieRequest){
        sortMethods.get(field(movieRequest) + direction(movieRequest)).accept(movieDaoAll);
    }

    private String field(MovieRequest defaultMovieRequest) {
        return Objects.toString(defaultMovieRequest.getSortField()) ;
    }

    private String direction(MovieRequest defaultMovieRequest) {
        return Objects.toString(defaultMovieRequest.getSortDirection());
    }
}
