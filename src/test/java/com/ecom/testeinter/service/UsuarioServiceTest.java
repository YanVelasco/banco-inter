package com.ecom.testeinter.service;

import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private PessoaFisica usuario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new PessoaFisica();
        usuario.setId(UUID.randomUUID());
        usuario.setEmail("teste@teste.com");
        usuario.setDocumento("12345678901");
        usuario.setNomeCompleto("Teste Usuario");
    }

    @Test
    public void testCadastrarUsuarioComSucesso() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.findByDocumento(usuario.getDocumento())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);

        assertNotNull(novoUsuario);
        assertEquals(usuario.getEmail(), novoUsuario.getEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testCadastrarUsuarioEmailDuplicado() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrarUsuario(usuario));

        assertEquals("E-mail já cadastrado.", exception.getMessage());
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    public void testBuscarUsuarioPorId() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        Usuario usuarioEncontrado = usuarioService.buscarPorId(usuario.getId()).orElseThrow();

        assertTrue(usuarioEncontrado instanceof PessoaFisica);
        PessoaFisica pessoaFisica = (PessoaFisica) usuarioEncontrado;

        assertNotNull(pessoaFisica);
        assertEquals(usuario.getId(), pessoaFisica.getId());
    }
}
