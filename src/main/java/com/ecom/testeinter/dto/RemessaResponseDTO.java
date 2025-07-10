package com.ecom.testeinter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Schema(description = "DTO para resposta de remessa entre usuários")
public class RemessaResponseDTO {

    @Schema(description = "ID do remetente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID remetenteId;

    @Schema(description = "ID do destinatário", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID destinatarioId;

    @Schema(description = "Valor convertido em dólares", example = "200.0")
    private double valorConvertidoDolares;
}
