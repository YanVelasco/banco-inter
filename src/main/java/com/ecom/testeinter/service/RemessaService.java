package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.LimiteDiarioExcedidoException;
import com.ecom.testeinter.exception.SaldoInsuficienteException;
import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.PessoaJuridica;
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
    public void realizarRemessa(Usuario remetente, Usuario destinatario, double valorReais) {
        Carteira carteiraRemetente = carteiraRepository.findById(remetente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira do remetente não encontrada."));

        if (carteiraRemetente.getSaldoReais() < valorReais) {
            throw new SaldoInsuficienteException("Saldo insuficiente na carteira do remetente.");
        }

        double limiteDiario = (remetente instanceof PessoaFisica) ? PessoaFisica.LIMITE_DIARIO : PessoaJuridica.LIMITE_DIARIO;
        if (valorReais > limiteDiario) {
            throw new LimiteDiarioExcedidoException("Valor excede o limite diário permitido para o usuário.");
        }

        Double cotacaoDolar = cotacaoService.obterCotacaoDolar(LocalDate.now());
        if (cotacaoDolar == null) {
            throw new IllegalStateException("Não foi possível obter a cotação do dólar.");
        }

        double valorDolares = valorReais / cotacaoDolar;

        carteiraRemetente.setSaldoReais(carteiraRemetente.getSaldoReais() - valorReais);
        carteiraRepository.save(carteiraRemetente);

        Carteira carteiraDestinatario = carteiraRepository.findById(destinatario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira do destinatário não encontrada."));

        carteiraDestinatario.setSaldoDolares(carteiraDestinatario.getSaldoDolares() + valorDolares);
        carteiraRepository.save(carteiraDestinatario);
    }
}
