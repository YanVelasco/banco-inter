package com.ecom.testeinter.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemessaRequestDTO {

    @NotNull(message = "O ID do remetente é obrigatório.")
    private UUID remetenteId;

    @NotNull(message = "O ID do destinatário é obrigatório.")
    private UUID destinatarioId;

    @Positive(message = "O valor da remessa deve ser positivo.")
    private double valorReais;

}
