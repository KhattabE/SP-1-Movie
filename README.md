# SP-1 Movie Repository

## Description

This project is a backend application that fetches Danish movie data from the TMDb API and stores it in a PostgreSQL database using JPA and Hibernate.

The database contains movies released within the last 5 years together with actors, directors and genres.

## Technologies

- Java
- Maven
- JPA / Hibernate
- PostgreSQL
- Jackson
- Lombok
- TMDb API
- JUnit
- Testcontainers

## Structure

The project uses DTOs, entities, DAOs and services.

Data flow:

TMDb API → DTO → Service → Entity → DAO → PostgreSQL

Main entities:

- Movie
- Actor
- Director
- Genre

## Functionality

The application can:

- Fetch Danish movies from TMDb
- Store movies, actors, directors and genres
- List movies
- Search movies by title
- Find movies by genre
- Add, update and delete movies
- Calculate average movie rating
- Find top 10 highest-rated movies
- Find top 10 lowest-rated movies
- Find top 10 most popular movies

## Database

After importing data from TMDb, the database contained:

- 1522 movies
- 4376 actors
- 914 directors
- 19 genres

The exact numbers can change depending on TMDb data.

## Setup

Create a PostgreSQL database called:

`sp1_movie`

The following environment variables are required:

- `JDBC_CONNECTION_STRING`
- `JDBC_PASSWORD`
- `TMDB_READ_ACCESS_TOKEN`

API tokens and passwords should not be pushed to GitHub.

## Testing

JUnit and Testcontainers are used to test the DAO and service layers.

## Group Members

Amaan
Khattab
Musa
Rasull
