package com.ecom.testeinter.service;

import com.ecom.testeinter.exception.LimiteDiarioExcedidoException;
import com.ecom.testeinter.exception.SaldoInsuficienteException;
import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.PessoaJuridica;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void realizarRemessa_ComSaldoInsuficiente_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(50.0);

        when(carteiraRepository.findById(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 100.0));

        assertEquals("Saldo insuficiente na carteira do remetente.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComValorExcedendoLimite_DeveLancarExcecao() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(PessoaFisica.LIMITE_DIARIO + 1000.0);

        when(carteiraRepository.findById(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));

        LimiteDiarioExcedidoException exception = assertThrows(LimiteDiarioExcedidoException.class, () ->
                remessaService.realizarRemessa(remetente, new PessoaFisica(), PessoaFisica.LIMITE_DIARIO + 1));

        assertEquals("Valor excede o limite diário permitido para o usuário.", exception.getMessage());
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
                remessaService.realizarRemessa(remetente, new PessoaFisica(), 100.0));

        assertEquals("Não foi possível obter a cotação do dólar.", exception.getMessage());
    }

    @Test
    void realizarRemessa_ComDadosValidos_DeveRealizarTransferencia() {
        Usuario remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setSaldoReais(1000.0);

        Usuario destinatario = new PessoaFisica();
        destinatario.setId(UUID.randomUUID());
        Carteira carteiraDestinatario = new Carteira();
        carteiraDestinatario.setSaldoDolares(50.0);

        when(carteiraRepository.findById(remetente.getId())).thenReturn(Optional.of(carteiraRemetente));
        when(carteiraRepository.findById(destinatario.getId())).thenReturn(Optional.of(carteiraDestinatario));
        when(cotacaoService.obterCotacaoDolar(LocalDate.now())).thenReturn(5.0);

        remessaService.realizarRemessa(remetente, destinatario, 500.0);

        assertEquals(500.0, carteiraRemetente.getSaldoReais());
        assertEquals(150.0, carteiraDestinatario.getSaldoDolares());

        verify(carteiraRepository, times(1)).save(carteiraRemetente);
        verify(carteiraRepository, times(1)).save(carteiraDestinatario);
    }
}