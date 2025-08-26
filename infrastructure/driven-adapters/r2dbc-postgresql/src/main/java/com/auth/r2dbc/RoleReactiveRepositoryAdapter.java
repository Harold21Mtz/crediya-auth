package com.auth.r2dbc;

import com.auth.model.user.Role;
import com.auth.model.user.User;
import com.auth.model.user.gateways.RoleRepository;
import com.auth.model.user.gateways.UserRepository;
import com.auth.r2dbc.entity.RoleEntity;
import com.auth.r2dbc.entity.UserEntity;
import com.auth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Mono<Boolean> existsRole(Long roleId) {
        return repository.existsById(roleId);
    }
}
