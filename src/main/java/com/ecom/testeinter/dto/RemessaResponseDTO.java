package com.ecom.testeinter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class RemessaResponseDTO {

    private UUID remetenteId;
    private UUID destinatarioId;
    private double valorConvertidoDolares;

}
