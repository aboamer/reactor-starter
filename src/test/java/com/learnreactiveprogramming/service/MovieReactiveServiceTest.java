package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieReactiveServiceTest {

    MovieReactiveService movieReactiveService = new MovieReactiveService();

    @Test
    void getAllMoviesTest() {

        Flux<Movie> movieFlux = movieReactiveService.getAllMovies();

        StepVerifier.create(movieFlux).assertNext(movie -> {
            assertEquals("Batman Begins", movie.getMovie().getName());
            assertEquals(2, movie.getReviewList().size());
        }).assertNext(movie -> {
            assertEquals("The Dark Knight", movie.getMovie().getName());
            assertEquals(2, movie.getReviewList().size());
        }).assertNext(movie -> {
            assertEquals("Dark Knight Rises", movie.getMovie().getName());
            assertEquals(2, movie.getReviewList().size());
        }).verifyComplete();
    }

    @Test
    void getMovieByIdTest() {

        long movieId = 100L;

        Mono<Movie> movieMono = movieReactiveService.getMovieById(movieId).log();

        StepVerifier.create(movieMono).assertNext(movie -> {
            assertEquals(movie.getMovie().getName(), "Batman Begins");
            assertEquals(2, movie.getReviewList().size());
        }).verifyComplete();
    }
}
