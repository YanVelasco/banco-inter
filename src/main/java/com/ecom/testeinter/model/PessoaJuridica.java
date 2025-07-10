package com.ecom.testeinter.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
@DiscriminatorValue("PJ")
public class PessoaJuridica extends Usuario {

    @Column(name = "limite_diario", nullable = false)
    public static final double LIMITE_DIARIO = 50000.0;

    public static double getLimiteDiario() {
        return LIMITE_DIARIO;
    }
}
