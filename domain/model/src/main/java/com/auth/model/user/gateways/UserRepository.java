package com.auth.model.user.gateways;

import com.auth.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Void> saveUser(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findUserByDocument(String documentNumber);
}
