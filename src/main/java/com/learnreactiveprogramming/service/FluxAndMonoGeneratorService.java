package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    /**
     * This is a flux publisher
     * <p>
     * log is not mandatory, but it shows what is handled behind the scene
     * <p>
     * request(unbounded) in the logs means: give me all what you have in your pocket
     */
    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("amer", "tamer", "samer")).log();
    }

    public Mono<String> nameMono() {

        return Mono.just("amer").log();
    }

    /**
     * flatmap is used when a transformation returns a reactive type (flux or mono)
     */
    public Flux<String> namesFluxFlatMap(int stringLength) {

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    private Flux<String> splitString(String name) {

        return Flux.fromArray(name.split(""));
    }

    /**
     * if order matters, don't use flatmap in asynchronous context because it doesn't keep order - use concatMap instead
     */
    public Flux<String> namesFluxFlatMapAsync(int stringLength) {

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    /**
     * use concatMap if order matters in async operations - but it is slower than flatmap
     */
    public Flux<String> namesFluxConcatMapAsync(int stringLength) {

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    private Flux<String> splitStringWithDelay(String name) {

        int delay = new Random().nextInt(1000);
        return Flux.fromArray(name.split("")).delayElements(Duration.ofMillis(delay));
    }

    public Mono<List<String>> namesMonoFlatMap(int stringLength) {

        return Mono.just("amer")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    private Mono<List<String>> splitStringMono(String name) {

        return Mono.just(List.of(name.split("")));
    }

    /**
     * Mono to flux
     */
    public Flux<String> namesMonoFlatMapMany(int stringLength) {

        return Mono.just("amer")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    /**
     * use transform to extract functionality to functional interface
     */
    public Flux<String> namesFluxTransform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase).filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .transform(filterMap).flatMap(this::splitString).log();
    }

    /**
     * test default string if empty
     */
    public Flux<String> namesFluxTransformDefault(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase).filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .transform(filterMap).flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    /**
     * test default flux if empty
     */
    public Flux<String> namesFluxTransformDefaultFlux(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength)
                        .flatMap(this::splitString);

        Flux<String> defaultFlux = Flux.just("default").transform(filterMap).log();

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    /**
     * defFlux will be called only after abcFlux is completed
     */
    public Flux<String> namesConcat() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

//        abcFlux.concatWith(defFlux).log();

        return Flux.concat(abcFlux, defFlux).log();
    }

    /**
     * both fluxes are merged together in parallel
     */
    public Flux<String> namesMerge() {

        Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

//        abcFlux.concatWith(defFlux).log();

        return Flux.merge(abcFlux, defFlux).log();
    }

    /**
     * both fluxes are merged together sequentially
     */
    public Flux<String> namesMergeSeq() {

        Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

//        abcFlux.concatWith(defFlux).log();

        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> namesZip() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second);
    }

    public Flux<String> namesZipMap() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        Flux<String> _123Flux = Flux.just("1", "2", "3");
        Flux<String> _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux).map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4()).log();
    }

    public Flux<String> namesFluxEvents(int stringLength) {

        return Flux.fromIterable(List.of("amer", "tamer", "samer"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .doOnNext(name -> System.out.println("Name is : " + name))
                .doOnSubscribe(name -> System.out.println("in susbscription"))
                .doOnComplete(() -> System.out.println("on complete"))
                .doFinally(signalType -> System.out.println("inside do finally - signal : " + signalType))
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> {
            System.out.println("Name is " + name);
        });

        fluxAndMonoGeneratorService.nameMono().subscribe(name -> {
            System.out.println("Mono Name is " + name);
        });
    }
}
