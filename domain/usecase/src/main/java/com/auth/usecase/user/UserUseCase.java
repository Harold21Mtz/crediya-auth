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
                        .doOnSubscribe(sub -> logger.debug("Verificando existencia de correo: " + user.getEmail()))
                        .doOnSuccess(v -> logger.info("Correo válido: " + user.getEmail()))
                        .doOnError(err -> logger.error("Error al verificar correo: " + user.getEmail(), err))
                        .then(Mono.defer(() -> {
                            logger.debug("Verificando existencia del rol con id: " + user.getRoleId());
                            return existsRole(user.getRoleId())
                                    .doOnSuccess(v -> logger.info("Rol válido: " + user.getRoleId()));
                        }))
                        .then(Mono.defer(() -> {
                            logger.debug("Guardando usuario en la base de datos: " + user);
                            return userRepository.saveUser(user)
                                    .doOnSuccess(v -> logger.info("Usuario guardado con éxito: " + user.getEmail()))
                                    .doOnError(err -> logger.error("Error al guardar usuario: " + user.getEmail(), err));
                        }))
        );
    }

    public Mono<User> getUserByDocumentNumber(String documentNumber) {
        return userRepository.findUserByDocument(documentNumber)
                .doOnNext(foundUser -> logger.info("Usuario encontrado: " + foundUser.getName()))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("No existe el usuario buscado con documento: " + documentNumber, null);
                    return Mono.error(new ResourceNotFoundException("No existe el usuario con email: " + documentNumber));
                }));
    }

}
