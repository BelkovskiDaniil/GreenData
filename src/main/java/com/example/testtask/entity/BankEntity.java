package com.example.testtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
@Table(name = "banks")
public class BankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 255)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "bankIdentificationCode")
    private Long bankIdentificationCode;
}
