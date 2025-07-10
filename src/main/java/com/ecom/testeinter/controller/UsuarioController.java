package com.ecom.testeinter.controller;

import com.ecom.testeinter.dto.UsuarioRequestDTO;
import com.ecom.testeinter.dto.UsuarioResponseDTO;
import com.ecom.testeinter.mapper.UsuarioMapper;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            UsuarioResponseDTO responseDTO = usuarioMapper.toDTO(novoUsuario);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable UUID id) {
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);
        return usuarioOptional.map(usuario -> ResponseEntity.ok(usuarioMapper.toDTO(usuario))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
