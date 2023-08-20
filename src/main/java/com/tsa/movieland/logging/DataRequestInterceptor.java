package com.tsa.movieland.logging;

import com.tsa.movieland.security.service.ActiveUserHolder;
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
public class DataRequestInterceptor implements HandlerInterceptor, BaseInterceptor {

    private final ActiveUserHolder activeUserHolder;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        final String requestURI = request.getRequestURI();

        if (!requestURI.endsWith(LOGIN) && !requestURI.endsWith(LOGOUT)) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            String authHeader = request.getHeader(AUTHORIZATION);
            if (Objects.nonNull(authHeader)) {
                final String userEmail = activeUserHolder.getUserEmail(authHeader);
                MDC.put(SPECIFIER, userEmail);
            } else {
                MDC.put(SPECIFIER, GUEST);
                log.info("Unknown user tried to get access to {}", requestURI);
            }
        }
        return true;
    }
}
