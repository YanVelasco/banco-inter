package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.UsuarioNotFoundException;
import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.CarteiraRepository;
import com.ecom.testeinter.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CarteiraRepository carteiraRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(UsuarioRepository usuarioRepository, CarteiraRepository carteiraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carteiraRepository = carteiraRepository;
    }

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

    public void adicionarSaldo(UUID usuarioId, double valor, String moeda) {
        logger.info("Adicionando saldo. Usuário ID: {}, Valor: {}, Moeda: {}", usuarioId, valor, moeda);

        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carteira novaCarteira = new Carteira();
                    novaCarteira.setUsuarioId(usuarioId);
                    novaCarteira.setSaldoReais(0.0);
                    novaCarteira.setSaldoDolares(0.0);
                    return carteiraRepository.save(novaCarteira);
                });

        logger.info("Valor do parâmetro 'moeda' recebido: '{}'", moeda);
        logger.info("Comparação com 'reais': {}", "reais".equalsIgnoreCase(moeda));
        logger.info("Comparação com 'dolares': {}", "dolares".equalsIgnoreCase(moeda));

        if ("reais".equalsIgnoreCase(moeda)) {
            logger.info("Adicionando saldo em reais.");
            carteira.setSaldoReais(carteira.getSaldoReais() + valor);
        } else if ("dolares".equalsIgnoreCase(moeda)) {
            logger.info("Adicionando saldo em dólares.");
            carteira.setSaldoDolares(carteira.getSaldoDolares() + valor);
        } else {
            logger.error("Moeda inválida: {}", moeda);
            throw new IllegalArgumentException("Moeda inválida. Use 'reais' ou 'dolares'.");
        }

        carteiraRepository.save(carteira);
    }
}
