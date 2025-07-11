package com.ecom.testeinter.controller;

import com.ecom.testeinter.dto.AdicionarSaldoRequestDTO;
import com.ecom.testeinter.dto.UsuarioRequestDTO;
import com.ecom.testeinter.dto.UsuarioResponseDTO;
import com.ecom.testeinter.exception.UsuarioNotFoundException;
import com.ecom.testeinter.mapper.UsuarioMapper;
import com.ecom.testeinter.model.DocumentoValidator;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @Operation(summary = "Cadastrar um novo usuário", description = "Cadastra um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        try {
            logger.info("Recebendo requisição para cadastrar usuário: {}", usuarioRequestDTO);

            if (usuarioRequestDTO.getDocumento().length() == 11 && !DocumentoValidator.isCPF(usuarioRequestDTO.getDocumento())) {
                logger.error("CPF inválido: {}", usuarioRequestDTO.getDocumento());
                throw new IllegalArgumentException("CPF inválido.");
            }
            if (usuarioRequestDTO.getDocumento().length() == 14 && !DocumentoValidator.isCNPJ(usuarioRequestDTO.getDocumento())) {
                logger.error("CNPJ inválido: {}", usuarioRequestDTO.getDocumento());
                throw new IllegalArgumentException("CNPJ inválido.");
            }

            Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            UsuarioResponseDTO responseDTO = usuarioMapper.toDTO(novoUsuario);
            logger.info("Usuário cadastrado com sucesso: {}", responseDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao cadastrar usuário: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable UUID id) {
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);
        return usuarioOptional.map(usuario -> ResponseEntity.ok(usuarioMapper.toDTO(usuario))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Adicionar saldo à carteira", description = "Adiciona saldo em reais ou dólares à carteira de um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo adicionado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PostMapping("/{id}/adicionar-saldo")
    public ResponseEntity<Void> adicionarSaldo(@PathVariable UUID id, @RequestBody AdicionarSaldoRequestDTO request) {
        try {
            usuarioService.adicionarSaldo(id, request.getValor(), request.getMoeda());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
