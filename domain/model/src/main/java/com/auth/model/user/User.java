package com.auth.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long userId;
    private String name;
    private String lastname;
    private LocalDate birthDate;
    private String documentNumber;
    private String phone;
    private String email;
    private String address;
    private BigDecimal baseSalary;
    private Long roleId;

}
