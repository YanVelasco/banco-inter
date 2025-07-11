package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.SaldoInsuficienteException;
import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.CarteiraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RemessaService {

    private final CarteiraRepository carteiraRepository;

    private final CotacaoService cotacaoService;

    public RemessaService(CarteiraRepository carteiraRepository, CotacaoService cotacaoService) {
        this.carteiraRepository = carteiraRepository;
        this.cotacaoService = cotacaoService;
    }

    @Transactional
    public void realizarRemessa(Usuario remetente, Usuario destinatario, double valor, String moeda) {
        Carteira carteiraRemetente = carteiraRepository.findByUsuarioId(remetente.getId())
                .orElseGet(() -> {
                    Carteira novaCarteira = new Carteira();
                    novaCarteira.setUsuarioId(remetente.getId());
                    novaCarteira.setSaldoReais(0.0);
                    novaCarteira.setSaldoDolares(0.0);
                    return carteiraRepository.save(novaCarteira);
                });

        Carteira carteiraDestinatario = carteiraRepository.findByUsuarioId(destinatario.getId())
                .orElseGet(() -> {
                    Carteira novaCarteira = new Carteira();
                    novaCarteira.setUsuarioId(destinatario.getId());
                    novaCarteira.setSaldoReais(0.0);
                    novaCarteira.setSaldoDolares(0.0);
                    return carteiraRepository.save(novaCarteira);
                });

        Double cotacaoDolar = cotacaoService.obterCotacaoDolar(LocalDate.now());
        if (cotacaoDolar == null) {
            throw new IllegalStateException("Não foi possível obter a cotação do dólar.");
        }

        if ("reais".equalsIgnoreCase(moeda)) {
            if (carteiraRemetente.getSaldoReais() >= valor) {
                carteiraRemetente.setSaldoReais(carteiraRemetente.getSaldoReais() - valor);
            } else {
                throw new SaldoInsuficienteException("Saldo insuficiente em reais na carteira do remetente.");
            }
        } else if ("dolares".equalsIgnoreCase(moeda)) {
            double valorConvertidoReais = valor * cotacaoDolar;
            if (carteiraRemetente.getSaldoDolares() >= valor) {
                carteiraRemetente.setSaldoDolares(carteiraRemetente.getSaldoDolares() - valor);
                valor = valorConvertidoReais;
            } else {
                throw new SaldoInsuficienteException("Saldo insuficiente em dólares na carteira do remetente.");
            }
        } else {
            throw new IllegalArgumentException("Moeda inválida. Use 'reais' ou 'dolares'.");
        }

        carteiraDestinatario.setSaldoReais(carteiraDestinatario.getSaldoReais() + valor);

        carteiraRepository.save(carteiraRemetente);
        carteiraRepository.save(carteiraDestinatario);
    }
}
