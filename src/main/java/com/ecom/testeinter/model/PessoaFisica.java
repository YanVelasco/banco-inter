package com.ecom.testeinter.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
@DiscriminatorValue("PF")
public class PessoaFisica extends Usuario {

    @Column(name = "limite_diario", nullable = false)
    public static final double LIMITE_DIARIO = 10000.0;

    public static double getLimiteDiario() {
        return LIMITE_DIARIO;
    }
}
