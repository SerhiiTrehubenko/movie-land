package com.tsa.movieland.config;

import com.tsa.movieland.common.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.Objects;

public class MovieRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return MovieRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(request);
        Map<String, String[]> parameters = request.getParameterMap();
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            return getMovieRequest(parameters);
        }
        return getEmptyMovieRequest();
    }

    private MovieRequest getMovieRequest(Map<String, String[]> parameters) {
        return parameters.entrySet().stream()
                .map(this::extractMovieRequest)
                .findFirst().orElse(getEmptyMovieRequest());
    }

    private MovieRequest extractMovieRequest(Map.Entry<String, String[]> entry) {
        String field = entry.getKey();
        String direction = entry.getValue()[0];
        try {
            return MovieRequest.builder()
                    .sortField(SortField.valueOf(field.toUpperCase()))
                    .sortDirection(SortDirection.valueOf(direction.toUpperCase()))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided Request Parameters should has format Field:{price/rating}" +
                    " you provided Field: [%s], Direction:{asc/desc} you provided Direction: [%s]".formatted(field, direction), e);
        }
    }

    private MovieRequest getEmptyMovieRequest() {
        return MovieRequest.EMPTY_MOVIE_REQUEST;
    }
}
