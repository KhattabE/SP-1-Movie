package app.entities;

public class Movie {

    /**
     * Fields:
     * id
     * tmdbId
     * title
     * releaseDate
     * rating
     * popularity
     * actors
     * director
     * genres
     *
     * Relationships:
     * actors   -> @ManyToMany with Actor
     * director -> @ManyToOne with Director
     * genres   -> @ManyToMany with Genre
     *
     * Methods:
     * getters/setters
     * constructors
     */

    /**
     -Why?
     ------------------------------
     Movie ↔ Actor
     @ManyToMany

     One movie has many actors.
     One actor can appear in many movies.
     ------------------------------

     ------------------------------
     Movie → Director
     @ManyToOne

     One movie has one director.
     One director can direct many movies.
     ------------------------------

     ------------------------------
     Movie ↔ Genre
     @ManyToMany

     One movie can have several genres.
     One genre can belong to many movies.
     ------------------------------

     */

}
