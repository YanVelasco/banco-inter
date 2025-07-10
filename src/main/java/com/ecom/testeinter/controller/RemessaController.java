package com.ecom.testeinter.controller;

import com.ecom.testeinter.dto.RemessaRequestDTO;
import com.ecom.testeinter.dto.RemessaResponseDTO;
import com.ecom.testeinter.exception.UsuarioNotFoundException;
import com.ecom.testeinter.mapper.RemessaMapper;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.service.CotacaoService;
import com.ecom.testeinter.service.RemessaService;
import com.ecom.testeinter.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/remessas")
public class RemessaController {

    private final RemessaService remessaService;

    private final UsuarioService usuarioService;

    private final RemessaMapper remessaMapper;

    private final CotacaoService cotacaoService;

    public RemessaController(RemessaService remessaService, UsuarioService usuarioService, RemessaMapper remessaMapper, CotacaoService cotacaoService) {
        this.remessaService = remessaService;
        this.usuarioService = usuarioService;
        this.remessaMapper = remessaMapper;
        this.cotacaoService = cotacaoService;
    }

    @Operation(summary = "Realizar remessa entre usuários", description = "Realiza uma remessa entre usuários com conversão de moeda de Real para Dólar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remessa realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RemessaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RemessaResponseDTO> realizarRemessa(@Valid @RequestBody RemessaRequestDTO remessaRequestDTO) {
        try {
            Usuario remetente = usuarioService.buscarPorId(remessaRequestDTO.getRemetenteId()).orElseThrow(() -> new UsuarioNotFoundException("Remetente não encontrado."));
            Usuario destinatario = usuarioService.buscarPorId(remessaRequestDTO.getDestinatarioId()).orElseThrow(() -> new UsuarioNotFoundException("Destinatário não encontrado."));

            remessaService.realizarRemessa(remetente, destinatario, remessaRequestDTO.getValorReais());

            double valorConvertidoDolares = remessaRequestDTO.getValorReais() / cotacaoService.obterCotacaoDolar(LocalDate.now());
            RemessaResponseDTO responseDTO = remessaMapper.toResponseDTO(remessaRequestDTO, valorConvertidoDolares, remetente, destinatario);

            return ResponseEntity.ok(responseDTO);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
