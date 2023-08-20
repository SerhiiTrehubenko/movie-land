package com.tsa.movieland.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class LoginInterceptor implements HandlerInterceptor, BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (request.getRequestURI().endsWith(LOGIN)) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            MDC.put(SPECIFIER, GUEST);
        }
        return true;
    }
}
