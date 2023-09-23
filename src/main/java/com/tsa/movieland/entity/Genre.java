package com.tsa.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Builder
@Getter
@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "Genre.movies",
        attributeNodes = @NamedAttributeNode("movies")
)
public class Genre {
    @Id
    @SequenceGenerator(
            name = "genres_id",
            sequenceName = "genres_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genres_id"
    )
    @Column(name = "genre_id")
    private Integer id;
    @Column(name = "genre_name")
    private String name;

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "movies_genres",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Movie> movies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        return new EqualsBuilder().append(id, genre.id).append(name, genre.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).toHashCode();
    }
}
