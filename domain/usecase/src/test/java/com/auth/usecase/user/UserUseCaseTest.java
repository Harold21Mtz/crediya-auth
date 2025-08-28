package com.auth.usecase.user;

import com.auth.model.role.gateways.RoleRepository;
import com.auth.model.user.User;
import com.auth.model.user.gateways.UserRepository;
import com.auth.model.utils.TransactionalWrapper;
import com.auth.model.utils.UserCaseLogger;
import com.auth.usecase.user.exception.ConflictException;
import com.auth.usecase.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserCaseLogger logger;

    @Mock
    private TransactionalWrapper transactional;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .userId(1L)
                .name("Harold")
                .lastname("Martinez")
                .birthDate(LocalDate.of(2000, 6, 7))
                .documentNumber("1000000000")
                .phone("3114567890")
                .email("harold@gmail.com")
                .address("Calle 7")
                .baseSalary(new BigDecimal("1500000.00"))
                .roleId(1L)
                .build();

        when(transactional.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void existsEmail_shouldReturnError_whenEmailExists() {
        when(userRepository.existsByEmail("harold@gmail.com")).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.existsEmail("harold@gmail.com"))
                .expectError(ConflictException.class)
                .verify();

        verify(userRepository).existsByEmail("harold@gmail.com");
    }

    @Test
    void existsEmail_shouldReturnEmpty_whenEmailNotExists() {
        when(userRepository.existsByEmail("martinez@gmail.com")).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.existsEmail("martinez@gmail.com"))
                .verifyComplete();

        verify(userRepository).existsByEmail("martinez@gmail.com");
    }

    @Test
    void existsRole_shouldReturnEmpty_whenRoleExists() {
        when(roleRepository.existsRole(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.existsRole(1L))
                .verifyComplete();

        verify(roleRepository).existsRole(1L);
    }

    @Test
    void existsRole_shouldReturnError_whenRoleNotExists() {
        when(roleRepository.existsRole(2L)).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.existsRole(2L))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(roleRepository).existsRole(2L);
    }

    @Test
    void createUser_shouldSaveUser_whenValid() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsRole(user.getRoleId())).thenReturn(Mono.just(true));
        when(userRepository.saveUser(user)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createUser(user))
                .verifyComplete();

        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository).existsRole(user.getRoleId());
        verify(userRepository).saveUser(user);
        verify(logger).trace("Inicio de creación de usuario");
        verify(logger).info("Usuario creado con correo: " + user.getEmail());
    }
}
