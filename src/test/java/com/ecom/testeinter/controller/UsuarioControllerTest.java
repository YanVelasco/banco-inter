package com.ecom.testeinter.controller;

import com.ecom.testeinter.dto.UsuarioRequestDTO;
import com.ecom.testeinter.dto.UsuarioResponseDTO;
import com.ecom.testeinter.mapper.UsuarioMapper;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioMapper usuarioMapper;

    private PessoaFisica usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new PessoaFisica();
        usuario.setId(UUID.randomUUID());
        usuario.setEmail("teste@teste.com");
        usuario.setDocumento("12345678901");
        usuario.setNomeCompleto("Teste Usuario");

        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setEmail("teste@teste.com");
        usuarioRequestDTO.setDocumento("12345678901");
        usuarioRequestDTO.setNomeCompleto("Teste Usuario");

        usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setDocumento(usuario.getDocumento());
        usuarioResponseDTO.setNomeCompleto(usuario.getNomeCompleto());
    }

    @Test
    public void testCadastrarUsuarioComSucesso() {
        when(usuarioMapper.toEntity(usuarioRequestDTO)).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioResponseDTO);

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.cadastrarUsuario(usuarioRequestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDTO, response.getBody());
    }

    @Test
    public void testBuscarUsuarioPorId() {
        when(usuarioService.buscarPorId(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioResponseDTO);

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.buscarUsuarioPorId(usuario.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDTO, response.getBody());
    }
}
