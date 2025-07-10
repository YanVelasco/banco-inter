package com.ecom.testeinter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "DTO para requisição de cadastro de usuário")
public class UsuarioRequestDTO {

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres.")
    private String nomeCompleto;

    @Schema(description = "Email do usuário", example = "joao@teste.com")
    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email deve ser válido.")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456")
    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    @Schema(description = "Documento do usuário (CPF ou CNPJ)", example = "12345678901")
    @NotBlank(message = "O documento é obrigatório.")
    private String documento;
}
