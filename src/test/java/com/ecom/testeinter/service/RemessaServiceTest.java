package com.ecom.testeinter.service;

import com.ecom.testeinter.model.Carteira;
import com.ecom.testeinter.model.PessoaFisica;
import com.ecom.testeinter.model.PessoaJuridica;
import com.ecom.testeinter.model.Usuario;
import com.ecom.testeinter.repository.CarteiraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RemessaServiceTest {

    @Autowired
    private RemessaService remessaService;

    @Autowired
    private CarteiraRepository carteiraRepository;

    private Usuario remetente;
    private Usuario destinatario;

    @BeforeEach
    public void setup() {
        remetente = new PessoaFisica();
        remetente.setId(UUID.randomUUID());
        remetente.setNomeCompleto("Remetente Teste");
        remetente.setEmail("remetente@teste.com");
        remetente.setDocumento("12345678901");

        destinatario = new PessoaJuridica();
        destinatario.setId(UUID.randomUUID());
        destinatario.setNomeCompleto("Destinatário Teste");
        destinatario.setEmail("destinatario@teste.com");
        destinatario.setDocumento("9876543210001");

        Carteira carteiraRemetente = new Carteira();
        carteiraRemetente.setId(remetente.getId());
        carteiraRemetente.setSaldoReais(20000.0);
        carteiraRemetente.setSaldoDolares(0.0);
        carteiraRemetente.setUsuario(remetente);
        carteiraRepository.save(carteiraRemetente);

        Carteira carteiraDestinatario = new Carteira();
        carteiraDestinatario.setId(destinatario.getId());
        carteiraDestinatario.setSaldoReais(0.0);
        carteiraDestinatario.setSaldoDolares(0.0);
        carteiraDestinatario.setUsuario(destinatario);
        carteiraRepository.save(carteiraDestinatario);
    }

    @Test
    public void testRealizarRemessaComSucesso() {
        remessaService.realizarRemessa(remetente, destinatario, 5000.0);

        Carteira carteiraRemetente = carteiraRepository.findById(remetente.getId()).orElseThrow();
        Carteira carteiraDestinatario = carteiraRepository.findById(destinatario.getId()).orElseThrow();

        assertEquals(15000.0, carteiraRemetente.getSaldoReais(), "O saldo em reais do remetente deve ser atualizado.");
        assertTrue(carteiraDestinatario.getSaldoDolares() > 0, "O saldo em dólares do destinatário deve ser atualizado.");
    }

    @Test
    public void testRealizarRemessaSaldoInsuficiente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            remessaService.realizarRemessa(remetente, destinatario, 25000.0);
        });

        assertEquals("Saldo insuficiente na carteira do remetente.", exception.getMessage());
    }

    @Test
    public void testRealizarRemessaExcedeLimiteDiarioPF() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            remessaService.realizarRemessa(remetente, destinatario, 15000.0);
        });

        assertEquals("Valor excede o limite diário permitido para o usuário.", exception.getMessage());
    }

    @Test
    public void testRealizarRemessaExcedeLimiteDiarioPJ() {
        remetente = destinatario; // Configura o remetente como PJ para este teste
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            remessaService.realizarRemessa(remetente, destinatario, 60000.0);
        });

        assertEquals("Valor excede o limite diário permitido para o usuário.", exception.getMessage());
    }

    @Test
    public void testTransacaoRevertidaEmCasoDeFalha() {
        Carteira carteiraRemetente = carteiraRepository.findById(remetente.getId()).orElseThrow();
        double saldoInicial = carteiraRemetente.getSaldoReais();

        assertThrows(IllegalArgumentException.class, () -> {
            remessaService.realizarRemessa(remetente, destinatario, saldoInicial + 1);
        });

        Carteira carteiraRemetenteAtualizada = carteiraRepository.findById(remetente.getId()).orElseThrow();
        assertEquals(saldoInicial, carteiraRemetenteAtualizada.getSaldoReais(), "O saldo do remetente deve permanecer inalterado após falha na transação.");
    }
}
