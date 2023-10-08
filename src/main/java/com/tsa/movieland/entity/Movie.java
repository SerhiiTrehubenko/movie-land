package com.tsa.movieland.entity;

import com.tsa.movieland.exception.BadPosterUpdateAttributeException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@NamedEntityGraphs(
        value = {
                @NamedEntityGraph(name = "Movie.byId",
                        attributeNodes = {
                                @NamedAttributeNode(value = "movieCountries", subgraph = "movieCountries.subgraph"),
                                @NamedAttributeNode(value = "movieGenres", subgraph = "movieGenres.subgraph"),
                                @NamedAttributeNode("posters"),
                        },
                        subgraphs = {
                                @NamedSubgraph(name = "movieCountries.subgraph",
                                        attributeNodes = @NamedAttributeNode(value = "country")),
                                @NamedSubgraph(name = "movieGenres.subgraph",
                                        attributeNodes = @NamedAttributeNode(value = "genre"))
                        }),
                @NamedEntityGraph(name = "Movie.byId.Poster",
                        attributeNodes = {
                                @NamedAttributeNode("posters"),
                        })
        })
public class Movie {
    @Id
    @SequenceGenerator(
            name = "movies_id",
            sequenceName = "movies_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movies_id"
    )
    @Column(name = "movie_id")
    private Integer id;
    @Column(name = "movie_rus_name")
    private String nameRussian;
    @Column(name = "movie_native_name")
    private String nameNative;
    @Column(name = "movie_release_year")
    private Integer yearOfRelease;
    @Column(name = "movie_description")
    private String description;
    @Column(name = "movie_price")
    private Double price;
    @Formula("(SELECT avg(mr.movie_raring) FROM movies_ratings mr WHERE mr.movie_id = movie_id GROUP BY movie_id)")
    private Double rating;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "posters",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "poster_link")
    private List<String> posters;

    @OneToMany(
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",
            insertable = false,
            updatable = false,
            nullable = false)
    private List<MovieCountry> movieCountries;

    @OneToMany(cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",
            insertable = false,
            updatable = false,
            nullable = false)
    private List<MovieGenre> movieGenres;

    public void setCountries(List<Integer> countriesId) {
        if (Objects.nonNull(countriesId)) {
            HashSet<MovieCountry> countriesNew = countriesId.stream().map(countryId -> MovieCountry.builder().movieId(id).countryId(countryId).build())
                    .collect(Collectors.toCollection(HashSet::new));
            if (Objects.isNull(movieCountries)) {
                movieCountries = new ArrayList<>(countriesNew);
            } else {
                HashSet<MovieCountry> countriesOld = new HashSet<>(movieCountries);
                countriesOld.addAll(countriesNew);
                countriesOld.retainAll(countriesNew);
                movieCountries.clear();
                movieCountries.addAll(countriesOld);
            }
        }
    }

    public void setGenres(List<Integer> genresId) {
        if (Objects.nonNull(genresId)) {
            HashSet<MovieGenre> genresNew = genresId.stream().map(countryId -> MovieGenre.builder().movieId(id).genreId(countryId).build())
                    .collect(Collectors.toCollection(HashSet::new));
            if (Objects.isNull(movieGenres)) {
                movieGenres = new ArrayList<>(genresNew);
            } else {
                HashSet<MovieGenre> genresOld = new HashSet<>(movieGenres);
                genresOld.addAll(genresNew);
                genresOld.retainAll(genresNew);
                movieGenres.clear();
                movieGenres.addAll(genresOld);
            }
        }
    }

    public void setPoster(String picturePath) {
        if (Objects.nonNull(picturePath)) {
            String[] pathWithOrderNumber = picturePath.split(";");
            String newPosterLink = pathWithOrderNumber[0].trim();
            if (Objects.nonNull(posters)) {
                if (pathWithOrderNumber.length != 2) {
                    posters.add(newPosterLink);
                } else {
                    updateLink(pathWithOrderNumber, newPosterLink);
                }
            } else {
                posters = Arrays.asList(newPosterLink);
            }
        }
    }

    private void updateLink(String[] pathWithOrderNumber, String newPosterLink) {
        int orderNumberPosterToUpdate = Integer.parseInt(pathWithOrderNumber[1].trim());
        if (orderNumberPosterToUpdate < 0 || orderNumberPosterToUpdate >= posters.size()) {
            throw new BadPosterUpdateAttributeException("Size of a Posters list is: [%d], you provided poster number: [%d] to be updated".formatted(posters.size(), orderNumberPosterToUpdate));
        }
        posters.set(orderNumberPosterToUpdate, newPosterLink);
    }
}
