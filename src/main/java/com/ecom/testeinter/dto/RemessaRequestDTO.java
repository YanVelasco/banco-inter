package com.ecom.testeinter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RemessaRequestDTO {

    @Schema(description = "ID do remetente", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "O ID do remetente é obrigatório.")
    private UUID remetenteId;

    @Schema(description = "ID do destinatário", example = "123e4567-e89b-12d3-a456-426614174001")
    @NotNull(message = "O ID do destinatário é obrigatório.")
    private UUID destinatarioId;

    @Schema(description = "Valor em reais a ser transferido", example = "1000.0")
    @NotNull(message = "O valor em reais é obrigatório.")
    @Positive(message = "O valor da remessa deve ser positivo.")
    private double valorReais;

    @Schema(description = "Moeda da remessa (reais ou dolares)", example = "reais")
    @NotNull(message = "A moeda é obrigatória.")
    private String moeda;
}
