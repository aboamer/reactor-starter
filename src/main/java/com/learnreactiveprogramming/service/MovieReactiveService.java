package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class MovieReactiveService {

    MovieInfoService movieInfoService = new MovieInfoService();
    ReviewService reviewService = new ReviewService();

    public Flux<Movie> getAllMovies() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {

            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
            return reviewsMono.map(reviewList -> new Movie(movieInfo, reviewList)).log();
        });
    }

    public Mono<Movie> getMovieById(long movieId) {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        Mono<List<Review>> reviewMono = reviewService.retrieveReviewsFlux(movieId).collectList();

        return movieInfoMono.flatMap(movieInfo -> {
            return reviewMono.map(reviews -> new Movie(movieInfo, reviews));
        });

//        return movieInfoMono.zipWith(reviewMono, Movie::new);
    }
}
