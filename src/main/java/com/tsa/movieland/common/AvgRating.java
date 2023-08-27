package com.tsa.movieland.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class AvgRating {

    private int movieId;
    private double currentAvg;
    private Map<String, Double> userVotes;

    public void updateAvgRating(String userEmail, double rating) {
        if (userVotes.containsKey(userEmail) && userVotes.get(userEmail) != rating) {
            userVotes.put(userEmail, rating);
            calculateAvg();
        } else if (!userVotes.containsKey(userEmail)) {
            userVotes.put(userEmail, rating);
            calculateAvg();
        }
    }

    private void calculateAvg() {
        Double total = userVotes.values().stream()
                .reduce(0.0, Double::sum);
        currentAvg = total / userVotes.size();
    }
}
