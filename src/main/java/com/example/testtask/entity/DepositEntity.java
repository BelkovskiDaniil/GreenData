package com.example.testtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "deposits")
public class DepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Max(50)
    @Column(name = "percent")
    private int percent;

    @NotNull
    @Column(name = "date_open")
    private Date dateOpen;

    @NotNull
    @Min(1)
    @Max(20)
    @Column(name = "month_period")
    private int monthPeriod;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private BankEntity bank;
}

