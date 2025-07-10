package com.ecom.testeinter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CotacaoServiceTest {

    @Autowired
    private CotacaoService cotacaoService;

    @Test
    public void testObterCotacaoDolarDiaUtil() {
        Double cotacao = cotacaoService.obterCotacaoDolar(LocalDate.of(2025, 7, 9)); // Dia útil
        assertNotNull(cotacao, "A cotação não deve ser nula para um dia útil.");
    }

    @Test
    public void testObterCotacaoDolarFinalDeSemana() {
        Double cotacao = cotacaoService.obterCotacaoDolar(LocalDate.of(2025, 7, 12)); // Sábado
        assertNotNull(cotacao, "A cotação não deve ser nula para um final de semana.");
    }
}
