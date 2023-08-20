package com.tsa.movieland.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class LogoutInterceptor implements HandlerInterceptor, BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (request.getRequestURI().endsWith(LOGOUT)) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            String authHeader = request.getHeader(AUTHORIZATION);
            if (Objects.isNull(authHeader)) {
                MDC.put(SPECIFIER, GUEST);
            }
        }
        return true;
    }
}
