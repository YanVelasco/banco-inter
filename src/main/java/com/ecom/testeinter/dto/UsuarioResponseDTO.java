package com.ecom.testeinter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Schema(description = "DTO para resposta de informações do usuário")
public class UsuarioResponseDTO {

    @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nomeCompleto;

    @Schema(description = "Email do usuário", example = "joao@teste.com")
    private String email;

    @Schema(description = "Documento do usuário (CPF ou CNPJ)", example = "12345678901")
    private String documento;
}
