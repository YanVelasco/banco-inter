package com.ecom.testeinter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdicionarSaldoRequestDTO {
    private double valor;
    private String moeda;
}
