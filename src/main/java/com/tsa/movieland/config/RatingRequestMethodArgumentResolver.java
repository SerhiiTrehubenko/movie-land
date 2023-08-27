package com.tsa.movieland.config;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.security.service.ActiveUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.BufferedReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class RatingRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ActiveUserHolder activeUserHolder;
    private final static Pattern PATH_VARIABLE = Pattern.compile("/\\d*/");
    private final static Pattern RATING = Pattern.compile("[\\d.]+");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RatingRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(request);
        int movieId = getMovieIdFromUrl(request);
        String userEmail = extractUserEmail(request);
        double rating = extractRating(request);

        return RatingRequest.builder()
                .userEmail(userEmail)
                .movieId(movieId)
                .rating(rating)
                .build();
    }

    private int getMovieIdFromUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        Matcher matcher = PATH_VARIABLE.matcher(url);
        if (matcher.find()) {
            String pathVariable = matcher.group().replaceAll("/", "");
            return Integer.parseInt(pathVariable);
        }
        throw new RuntimeException("provided url: [%s] does not have movie id".formatted(url));
    }

    private String extractUserEmail(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        return activeUserHolder.getUserEmail(header);
    }

    @SneakyThrows
    private double extractRating(HttpServletRequest request) {
        final BufferedReader reader = request.getReader();
        return reader.lines().filter(line -> line.contains("rating"))
                .map(line -> {
                    Matcher matcher = RATING.matcher(line);
                    if (matcher.find()) {
                        String rating = matcher.group();
                        return Double.parseDouble(rating);
                    }
                    return 0.0;
                }).findFirst()
                .orElseThrow(() -> new RuntimeException("Request body does not have rating"));
    }
}
