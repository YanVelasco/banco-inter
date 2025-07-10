package com.ecom.testeinter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UsuarioResponseDTO {

    private UUID id;
    private String nomeCompleto;
    private String email;
    private String documento;

}
