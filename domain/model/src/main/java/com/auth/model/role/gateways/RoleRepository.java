package com.auth.model.role.gateways;

import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Boolean> existsRole(Long roleId);
}
