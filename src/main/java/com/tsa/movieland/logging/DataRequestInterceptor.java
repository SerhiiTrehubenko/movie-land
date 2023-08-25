package com.tsa.movieland.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class DataRequestInterceptor implements HandlerInterceptor {

    private final static String REQUEST_ID = "requestId";
    private final static String SPECIFIER = "specifier";
    private final static String GUEST = "GUEST";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString());
        String authHeader = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(authHeader)) {
            MDC.put(SPECIFIER, GUEST);
        }
        return true;
    }
}
