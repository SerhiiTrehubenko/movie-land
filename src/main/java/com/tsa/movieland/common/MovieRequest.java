package com.tsa.movieland.common;

import com.tsa.movieland.currency.CurrencyType;
import lombok.*;

@Builder
@Getter
@ToString
public class MovieRequest {

    public final static MovieRequest EMPTY_MOVIE_REQUEST = MovieRequest.builder().build();

    private SortField sortField;
    private SortDirection sortDirection;
    private CurrencyType currencyType;
}

