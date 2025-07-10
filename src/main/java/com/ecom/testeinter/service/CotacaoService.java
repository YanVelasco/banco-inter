package com.ecom.testeinter.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CotacaoService {

    private static final String URL_TEMPLATE = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='%s'&$top=100&$format=json";

    private final RedisTemplate<String, Double> redisTemplate;

    private final RestTemplate restTemplate;

    public CotacaoService(RedisTemplate<String, Double> redisTemplate, RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }

    @Cacheable("cotacaoDolar")
    public Double obterCotacaoDolar(LocalDate data) {
        while (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            data = data.minusDays(1);
        }

        String cacheKey = "cotacaoDolar:" + data;

        Double cotacaoCache = redisTemplate.opsForValue().get(cacheKey);
        if (cotacaoCache != null) {
            return cotacaoCache;
        }

        String dataFormatada = data.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String url = String.format(URL_TEMPLATE, dataFormatada);

        try {
            Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);
            if (resposta != null && resposta.containsKey("value")) {
                List<Map<String, Object>> valores = (List<Map<String, Object>>) resposta.get("value");
                for (Map<String, Object> valor : valores) {
                    if (valor.containsKey("cotacaoCompra")) {
                        Double cotacao = (Double) valor.get("cotacaoCompra");

                        redisTemplate.opsForValue().set(cacheKey, cotacao, 1, TimeUnit.DAYS);
                        return cotacao;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter cotação: " + e.getMessage());
        }

        return null;
    }
}
