package com.tsa.movieland.controller;

import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.entity.User;
import com.tsa.movieland.security.service.ActiveUserHolder;
import com.tsa.movieland.service.ReviewService;
import com.tsa.movieland.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ActiveUserHolder activeUserHolder;

    @PostMapping
    public void addReview(@RequestBody AddReviewRequest reviewRequest,
                          HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION);
        final String userEmail = activeUserHolder.getUserEmail(header);
        final User userByEmail = userService.getUserByEmail(userEmail);
        reviewService.save(userByEmail.getId(), reviewRequest);
        log.info("User: [{}] with role: [{}] has added a review to a movie: [{}]",
                userEmail,
                userByEmail.getCredentials().getRole().name(),
                reviewRequest.getMovieId());
    }
}
