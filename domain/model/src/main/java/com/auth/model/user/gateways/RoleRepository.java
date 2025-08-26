package com.auth.model.user.gateways;

import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Boolean> existsRole(Long roleId);
}
