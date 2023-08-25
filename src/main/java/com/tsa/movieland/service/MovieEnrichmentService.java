package com.tsa.movieland.service;

import com.tsa.movieland.dto.MovieByIdDto;

public interface MovieEnrichmentService {
    MovieByIdDto enrich(MovieByIdDto movie);
}
