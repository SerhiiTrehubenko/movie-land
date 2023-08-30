package com.tsa.movieland.service;

import com.tsa.movieland.dto.MovieByIdDto;

import java.util.function.Supplier;

public interface MovieEnrichmentService {
    MovieByIdDto enrich(int movieId, Supplier<MovieByIdDto> movie);

}
