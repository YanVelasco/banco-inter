package com.ecom.testeinter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CotacaoServiceTest {

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private ValueOperations<String, Double> valueOperations;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CotacaoService cotacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void obterCotacaoDolar_ComCache_DeveRetornarCotacaoDoCache() {
        LocalDate data = LocalDate.now();
        String cacheKey = "cotacaoDolar:" + data;
        Double cotacaoEsperada = 5.25;

        when(valueOperations.get(cacheKey)).thenReturn(cotacaoEsperada);

        Double cotacao = cotacaoService.obterCotacaoDolar(data);

        assertNotNull(cotacao);
        assertEquals(cotacaoEsperada, cotacao);
        verify(valueOperations, times(1)).get(cacheKey);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void obterCotacaoDolar_SemCache_DeveRetornarCotacaoDaAPI() {
        LocalDate data = LocalDate.now();
        String cacheKey = "cotacaoDolar:" + data;
        Double cotacaoEsperada = 5.25;

        when(valueOperations.get(cacheKey)).thenReturn(null);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("value",
                List.of(Map.of("cotacaoCompra", cotacaoEsperada))));

        Double cotacao = cotacaoService.obterCotacaoDolar(data);

        assertNotNull(cotacao);
        assertEquals(cotacaoEsperada, cotacao);
        verify(valueOperations, times(1)).get(cacheKey);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
        verify(valueOperations, times(1)).set(cacheKey, cotacaoEsperada, 1, TimeUnit.DAYS);
    }

    @Test
    void obterCotacaoDolar_ComErroNaAPI_DeveRetornarNull() {
        LocalDate data = LocalDate.now();
        String cacheKey = "cotacaoDolar:" + data;

        when(valueOperations.get(cacheKey)).thenReturn(null);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("Erro na API"));

        Double cotacao = cotacaoService.obterCotacaoDolar(data);

        assertNull(cotacao);
        verify(valueOperations, times(1)).get(cacheKey);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void obterCotacaoDolar_ComFinalDeSemana_DeveAjustarData() {
        LocalDate sabado = LocalDate.of(2025, 7, 12); // Sábado
        LocalDate sextaFeira = LocalDate.of(2025, 7, 11); // Sexta-feira
        String cacheKey = "cotacaoDolar:" + sextaFeira;
        Double cotacaoEsperada = 5.25;

        when(valueOperations.get(cacheKey)).thenReturn(null);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("value",
                List.of(Map.of("cotacaoCompra", cotacaoEsperada))));

        Double cotacao = cotacaoService.obterCotacaoDolar(sabado);

        assertNotNull(cotacao);
        assertEquals(cotacaoEsperada, cotacao);
        verify(valueOperations, times(1)).get(cacheKey);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
        verify(valueOperations, times(1)).set(cacheKey, cotacaoEsperada, 1, TimeUnit.DAYS);
    }
}