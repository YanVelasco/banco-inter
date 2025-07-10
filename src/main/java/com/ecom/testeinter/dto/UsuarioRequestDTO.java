package com.ecom.testeinter.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email deve ser válido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    @NotBlank(message = "O documento é obrigatório.")
    private String documento;

}
