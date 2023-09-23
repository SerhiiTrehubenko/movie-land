package com.tsa.movieland.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Builder
@Getter
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Column(name = "user_id")
    @Id
    private int id;
    @Column(name = "user_nickname")
    private String nickname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        return new EqualsBuilder().append(id, userDto.id).append(nickname, userDto.nickname).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(nickname).toHashCode();
    }
}
