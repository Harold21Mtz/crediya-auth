package com.auth.usecase.user;

import com.auth.model.user.User;
import com.auth.model.role.gateways.RoleRepository;
import com.auth.model.user.gateways.UserRepository;
import com.auth.model.utils.TransactionalWrapper;
import com.auth.model.utils.UserCaseLogger;
import com.auth.usecase.exception.ConflictException;
import com.auth.usecase.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCaseLogger logger;
    private final TransactionalWrapper transactionalWrapper;

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
        logger.trace("Inicio de creación de usuario");

        return transactionalWrapper.transactional(
                existsEmail(user.getEmail())
                        .then(Mono.defer(() -> existsRole(user.getRoleId())))
                        .then(Mono.defer(() -> userRepository.saveUser(user)))
                        .doOnSuccess(v -> logger.info("Usuario creado con correo: " + user.getEmail()))
//                        .doOnError(err -> logger.error("Error en crear el usuario", err))
        );
    }
}
