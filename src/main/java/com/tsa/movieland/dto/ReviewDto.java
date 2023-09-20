package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Builder
@Getter
@ToString
public class ReviewDto {
    private UserDto user;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ReviewDto review = (ReviewDto) o;

        return new EqualsBuilder().append(user, review.user).append(text, review.text).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(user).append(text).toHashCode();
    }
}
