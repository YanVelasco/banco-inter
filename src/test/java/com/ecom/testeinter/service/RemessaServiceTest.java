package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.SaldoInsuficienteException;
import com.ecom.testeinter.exception.MoedaInvalidaException;
import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.CarteiraRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class RemessaServiceTest {

    @Mock
    private CarteiraRepository carteiraRepository;

    @Mock
    private CotacaoService cotacaoService;

    @InjectMocks
    private RemessaService remessaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(carteiraRepository.findByUsuarioId(any())).thenReturn(Optional.of(new Carteira()));
        when(carteiraRepository.findById(any())).thenReturn(Optional.of(new Carteira()));
        when(cotacaoService.obterCotacaoDolar(any())).thenReturn(5.0);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void realizarRemessa_ComCotacaoDolarNula_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(1000.0);

        when(carteiraRepository.findById(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));
        when(cotacaoService.obterCotacaoDolar(LocalDate.now())).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 100.0, "USD"));

        assertEquals("Não foi possível obter a cotação do dólar.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComSaldoInsuficienteEmReais_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(50.0);

        when(carteiraRepository.findByUsuarioId(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 100.0, "reais"));

        assertEquals("Saldo insuficiente em reais na carteira do remetente.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComSaldoInsuficienteEmDolares_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoDolares(5.0);

        when(carteiraRepository.findByUsuarioId(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 10.0, "dolares"));

        assertEquals("Saldo insuficiente em dólares na carteira do remetente.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComMoedaInvalida_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());

        MoedaInvalidaException exception = assertThrows(MoedaInvalidaException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 100.0, "euro"));

        assertEquals("Moeda inválida. Use 'reais' ou 'dolares'.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComSucessoEmReais_DeveAtualizarSaldos() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Usuario destinatario = new PessoaFisica();
        destinatario.setId(UUID.randomUUID());

        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(200.0);
        Carteira carteiraDestinatario = new Carteira();
        carteiraDestinatario.setSaldoReais(50.0);

        when(carteiraRepository.findByUsuarioId(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));
        when(carteiraRepository.findByUsuarioId(destinatario.getId())).thenReturn(Optional.of(carteiraDestinatario));

        remessaService.realizarRemessa(remetente, destinatario, 100.0, "reais");

        assertEquals(100.0, carteiraRemetente.getSaldoReais());
        assertEquals(150.0, carteiraDestinatario.getSaldoReais());
    }

    @Test
    void realizarRemessa_ComSucessoEmDolares_DeveAtualizarSaldos() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Usuario destinatario = new PessoaFisica();
        destinatario.setId(UUID.randomUUID());

        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoDolares(50.0);
        Carteira carteiraDestinatario = new Carteira();
        carteiraDestinatario.setSaldoReais(100.0);

        when(carteiraRepository.findByUsuarioId(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));
        when(carteiraRepository.findByUsuarioId(destinatario.getId())).thenReturn(Optional.of(carteiraDestinatario));

        remessaService.realizarRemessa(remetente, destinatario, 10.0, "dolares");

        assertEquals(40.0, carteiraRemetente.getSaldoDolares());
        assertEquals(150.0, carteiraDestinatario.getSaldoReais());
    }
}