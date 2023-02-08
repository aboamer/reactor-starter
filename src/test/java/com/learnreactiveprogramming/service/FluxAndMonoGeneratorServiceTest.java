package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux).expectNext("amer", "tamer", "samer").verifyComplete();

        StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();

        StepVerifier.create(namesFlux).expectNext("amer").expectNextCount(2).verifyComplete();
    }

    @Test
    void namesMono() {

        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(namesMono).expectNext("amer").verifyComplete();

        StepVerifier.create(namesMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void namesFluxFlatMapTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap(4);

        StepVerifier.create(namesFlux).expectNext("T","A","M","E","R","S","A","M","E","R").verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsyncTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(4);

        StepVerifier.create(namesFlux).expectNextCount(10).verifyComplete();
    }

    @Test
    void namesFluxConcatMapAsyncTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMapAsync(4);

        StepVerifier.create(namesFlux).expectNext("T","A","M","E","R","S","A","M","E","R").expectNextCount(0).verifyComplete();
    }

    @Test
    void namesMonoFlatMapTest() {

        Mono<List<String>> namesMono = fluxAndMonoGeneratorService.namesMonoFlatMap(3);

        StepVerifier.create(namesMono).expectNext(List.of("A","M","E","R")).verifyComplete();
    }

    @Test
    void namesMonoFlatMapManyTest() {

        Flux<String> namesFluxFromMono = fluxAndMonoGeneratorService.namesMonoFlatMapMany(3);

        StepVerifier.create(namesFluxFromMono).expectNext("A","M","E","R").verifyComplete();
    }

    @Test
    void namesFluxTransformTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(4);

        StepVerifier.create(namesFlux).expectNext("T","A","M","E","R","S","A","M","E","R").verifyComplete();
    }

    @Test
    void namesFluxTransformDefaultTest() {

        // will return the default because I sent stringLength to be 8
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxTransformDefault(8);

        StepVerifier.create(namesFlux).expectNext("default").verifyComplete();
    }

    @Test
    void namesFluxTransformDefaultFluxTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFluxTransformDefaultFlux(6);

        StepVerifier.create(namesFlux).expectNext("D", "E","F","A","U","L","T").verifyComplete();
    }

    @Test
    void namesMergeTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesMerge();

        StepVerifier.create(namesFlux).expectNext("A", "D","B","E","C","F").verifyComplete();
    }

    @Test
    void namesMergeSeqTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesMergeSeq();

        StepVerifier.create(namesFlux).expectNext("A", "B","C","D","E","F").verifyComplete();
    }

    @Test
    void namesZipTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesZip();

        StepVerifier.create(namesFlux).expectNext("AD", "BE", "CF").verifyComplete();
    }

    @Test
    void namesZipMapTest() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesZipMap();

        StepVerifier.create(namesFlux).expectNext("AD14", "BE25", "CF36").verifyComplete();
    }
}
