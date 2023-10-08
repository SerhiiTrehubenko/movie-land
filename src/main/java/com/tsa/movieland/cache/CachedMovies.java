package com.tsa.movieland.cache;

import com.tsa.movieland.dto.MovieByIdDto;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CachedMovies {

    private final Map<Integer, SoftReference<MovieByIdDto>> cachedMovies = new ConcurrentHashMap<>();

    public MovieByIdDto getMovie(int movieId, Supplier<MovieByIdDto> supplier) {
        SoftReference<MovieByIdDto> softReferenceMovie = cachedMovies.compute(movieId, (id, valueOld) -> {
            if (Objects.nonNull(valueOld) && Objects.nonNull(valueOld.get())) {
                return valueOld;
            }
            return new SoftReference<>(supplier.get());
        });
        return softReferenceMovie.get();
    }

    public void removeFromCache(int movieId) {
        cachedMovies.remove(movieId);
    }
}
