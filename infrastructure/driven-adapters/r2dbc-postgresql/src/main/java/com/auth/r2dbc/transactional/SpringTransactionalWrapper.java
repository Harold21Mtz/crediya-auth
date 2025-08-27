package com.auth.r2dbc.transactional;

import com.auth.model.utils.TransactionalWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class SpringTransactionalWrapper implements TransactionalWrapper {

    private final TransactionalOperator txOperator;

    @Override
    public <T> Mono<T> transactional(Mono<T> publisher) {
        return txOperator.transactional(publisher);
    }

    @Override
    public <T> Flux<T> transactional(Flux<T> publisher) {
        return txOperator.transactional(publisher);
    }
}
