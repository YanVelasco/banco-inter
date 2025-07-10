package com.ecom.testeinter.mapper;

import com.ecom.testeinter.dto.UsuarioRequestDTO;
import com.ecom.testeinter.dto.UsuarioResponseDTO;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new PessoaFisica();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setDocumento(dto.getDocumento());
        return usuario;
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setDocumento(usuario.getDocumento());
        return dto;
    }
}
