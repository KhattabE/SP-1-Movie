package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "tmdb_id", unique = true, nullable = false)
     private Integer tmdbId;

     @Column(nullable = false)
     private String title;

     @Column(name = "release_date")
     private LocalDate releaseDate;

     private Double rating;

     private Double popularity;

     @ManyToMany
     @JoinTable(name = "movie_actor",
             joinColumns = @JoinColumn(name = "movie_id"),
             inverseJoinColumns = @JoinColumn(name = "actor_id")
     )

     @Builder.Default
     private List<Actor> actors = new ArrayList<>();

     @ManyToOne
     @JoinColumn(name = "director_id")
     private Director director;

     @ManyToMany
     @JoinTable(name = "movie_genre",
             joinColumns = @JoinColumn(name = "movie_id"),
             inverseJoinColumns = @JoinColumn(name = "genre_id")
     )
     @Builder.Default
     private List<Genre> genres = new ArrayList<>();

     public void addActor(Actor actor) {
          actors.add(actor);
     }

     public void addGenre(Genre genre) {
          genres.add(genre);
     }
}