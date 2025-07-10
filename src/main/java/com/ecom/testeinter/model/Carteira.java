package com.ecom.testeinter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carteira")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "saldo_reais", nullable = false)
    private double saldoReais;

    @Column(name = "saldo_dolares", nullable = false)
    private double saldoDolares;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private UUID usuarioId;

    @OneToOne(mappedBy = "carteira", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

}
