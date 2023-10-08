package com.tsa.movieland.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AvgRating {

    private Integer movieId;
    private Double currentAvg;
    private Long userVotes;

    public void updateAvgRating(double rating) {
        synchronized (this) {
            double addNewAvrRating = (rating / (userVotes + 1));
            double decreasePercent = (double) 1 / userVotes;
            double subtractedAvrRating = currentAvg / (1 + decreasePercent);
            currentAvg = subtractedAvrRating + addNewAvrRating;
            userVotes++;
        }
    }

    public double getCurrentAvg() {
        BigDecimal bigDecimal = new BigDecimal(currentAvg).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
