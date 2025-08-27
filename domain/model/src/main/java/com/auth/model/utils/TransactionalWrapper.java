package com.auth.model.utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionalWrapper {

    <T> Mono<T> transactional(Mono<T> publisher);

    <T> Flux<T> transactional(Flux<T> publisher);
}
