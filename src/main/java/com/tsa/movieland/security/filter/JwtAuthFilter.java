package com.tsa.movieland.security.filter;

import com.tsa.movieland.security.service.ActiveUserHolder;
import com.tsa.movieland.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final static String BEARER = "Bearer";
    private final UserService userDetailsService;
    private final ActiveUserHolder activeUserHolder;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String requestURI = request.getRequestURI();

        if (Objects.isNull(authHeader) ||
                !authHeader.startsWith(BEARER) ||
                requestURI.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = activeUserHolder.getUserEmail(authHeader);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(userEmail) &&
                Objects.isNull(authentication)) {

            UserDetails userDetails = userDetailsService.getUserByEmail(userEmail);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

            authenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request, response);
    }
}
