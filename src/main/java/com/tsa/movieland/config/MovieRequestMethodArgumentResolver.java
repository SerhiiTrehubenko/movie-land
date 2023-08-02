package com.tsa.movieland.config;

import com.tsa.movieland.common.*;
import com.tsa.movieland.currency.CurrencyType;
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

    private final static String PRICE_PARAMETER_NAME = "price";
    private final static String RATING_PARAMETER_NAME = "rating";
    private final static String CURRENCY_PARAMETER_NAME = "currency";

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

    private MovieRequest getEmptyMovieRequest() {
        return MovieRequest.EMPTY_MOVIE_REQUEST;
    }

    private MovieRequest getMovieRequest(Map<String, String[]> parameters) {
        return parameters.entrySet().stream()
                .map(this::extractMovieRequest)
                .findFirst().orElse(getEmptyMovieRequest());
    }

    private MovieRequest extractMovieRequest(Map.Entry<String, String[]> entry) {
        String parameter = entry.getKey();
        checkParameterName(parameter);
        String value = entry.getValue()[0];

        if (Objects.equals(parameter, CURRENCY_PARAMETER_NAME)) {
            return createMovieRequestCurrency(value);
        } else {
            return createMovieRequestSortRatingPrice(parameter, value);
        }
    }

    private void checkParameterName(String parameter) {
        if (!Objects.equals(RATING_PARAMETER_NAME, parameter) &&
                !Objects.equals(PRICE_PARAMETER_NAME, parameter) &&
                !Objects.equals(CURRENCY_PARAMETER_NAME, parameter)) {
            throw new IllegalArgumentException("Was provided illegal parameter: [%s]".formatted(parameter));
        }
    }

    private MovieRequest createMovieRequestCurrency(String value) {
        try {
            return MovieRequest.builder()
                    .currencyType(CurrencyType.valueOf(value.toUpperCase()))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided Request Parameters should be: {?currency=[usd / eur]}", e);
        }
    }

    private MovieRequest createMovieRequestSortRatingPrice(String parameter, String value) {
        try {
            return MovieRequest.builder()
                    .sortField(SortField.valueOf(parameter.toUpperCase()))
                    .sortDirection(SortDirection.valueOf(value.toUpperCase()))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided Request Parameters should has format Field:{price/rating}" +
                    " you provided Field: [%s], Direction:{asc/desc} you provided Direction: [%s]".formatted(parameter, value), e);
        }
    }
}
