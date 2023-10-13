package com.tsa.movieland.common;

import com.tsa.movieland.dto.MovieFindAllDto;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.tsa.movieland.common.SortDirection.*;

public class MovieSortCriteria {
    private final static String NULL = "null";
    private final static String RATING = SortField.RATING.name();
    private final static String PRICE = SortField.PRICE.name();
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_RATING = Comparator.comparing(MovieFindAllDto::getRating);
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_PRICE = Comparator.comparing(MovieFindAllDto::getPrice);
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_ID = Comparator.comparing(MovieFindAllDto::getId);

    private final Map<String, Comparator<MovieFindAllDto>> sortComparator = Map.of(
            RATING + ASC, COMPARATOR_BY_RATING,
            RATING + DESC, COMPARATOR_BY_RATING.reversed(),
            PRICE + ASC, COMPARATOR_BY_PRICE,
            PRICE + DESC, COMPARATOR_BY_PRICE.reversed(),
            NULL + NULL, COMPARATOR_BY_ID
    );

    public Comparator<MovieFindAllDto> getSortCriteria(MovieRequest movieRequest) {
        return sortComparator.get(field(movieRequest) + direction(movieRequest));
    }

    private String field(MovieRequest defaultMovieRequest) {
        return Objects.toString(defaultMovieRequest.getSortField());
    }

    private String direction(MovieRequest defaultMovieRequest) {
        return Objects.toString(defaultMovieRequest.getSortDirection());
    }
}
