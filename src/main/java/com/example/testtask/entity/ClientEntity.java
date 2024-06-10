package com.example.testtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

enum FormType {
    A, B, C;
}

@Data
@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 255)
    @Column(name = "name")
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 15)
    @Column(name = "short_name")
    private String shortName;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "address")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "form")
    private FormType form;
}
