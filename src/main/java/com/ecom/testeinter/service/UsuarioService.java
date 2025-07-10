package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.UsuarioNotFoundException;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (usuarioRepository.findByDocumento(usuario.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Documento já cadastrado.");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }
}
