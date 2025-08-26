package com.auth.usecase.user;

import com.auth.model.user.User;
import com.auth.model.user.gateways.RoleRepository;
import com.auth.model.user.gateways.UserRepository;
import com.auth.usecase.user.exception.ConflictException;
import com.auth.usecase.user.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<Void> existsEmail(String email) {
        return userRepository.existsByEmail(email)
                .flatMap(exists ->
                        exists
                                ? Mono.error(new ConflictException("El correo ya está registrado"))
                                : Mono.empty()
                );
    }

    public Mono<Void> existsRole(Long roleId) {
        return roleRepository.existsRole(roleId)
                .flatMap(exists ->
                        exists
                                ? Mono.empty()
                                : Mono.error(new ResourceNotFoundException("El rol ingresado no existe"))
                );
    }

    public Mono<Void> createUser(User user) {
        return existsEmail(user.getEmail())
                .then(existsRole(user.getRoleId()))
                .then(userRepository.saveUser(user))
                .then();
    }
}
