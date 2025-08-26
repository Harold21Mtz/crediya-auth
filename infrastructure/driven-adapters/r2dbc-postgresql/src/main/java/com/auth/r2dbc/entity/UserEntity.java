package com.auth.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("\"user\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column("user_id")
    private Long userId;
    private String name;
    @Column("lastname")
    private String lastname;
    @Column("birth_date")
    private LocalDate birthDate;
    private String documentNumber;
    private String phone;
    private String email;
    private String address;
    @Column("base_salary")
    private BigDecimal baseSalary;
    @Column("role_id")
    private Long roleId;
}
