# Serviço de Remessa - Desafio Inter

## Descrição
Este projeto é uma API RESTFul para realizar remessas entre usuários, com conversão de moeda de Real para Dólar. Ele utiliza Spring Boot, Postgres, Redis e integra com a API pública do Banco Central para obter a cotação do dólar.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Postgres**
- **Redis**
- **Docker** e **Docker Compose**
- **JUnit 5**
- **Swagger**

## Pré-requisitos
- Docker e Docker Compose instalados.
- JDK 17 instalado.
- Maven instalado.

## Configuração e Execução

### 1. Subir os serviços com Docker Compose
Na raiz do projeto, execute:
```bash
docker-compose up -d
```

### 2. Compilar e Executar a Aplicação
Para compilar e executar a aplicação, use o Maven:
```bash
./mvnw spring-boot:run
```

### 3. Testar a Aplicação
Para executar os testes unitários e de integração, use:
```bash
./mvnw test
```

### 4. Acessar a Documentação da API
Acesse a documentação da API gerada pelo Swagger em:
```
http://localhost:8080/swagger-ui.html
```

## Endpoints Principais

### 1. Cadastro de Usuários
**POST** `/api/usuarios`
- Body:
```json
{
  "nomeCompleto": "João Silva",
  "email": "joao@teste.com",
  "senha": "123456",
  "documento": "12345678901"
}
```
- Exemplo de Resposta:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "nomeCompleto": "João Silva",
  "email": "joao@teste.com",
  "documento": "12345678901"
}
```

### 2. Realizar Remessa
**POST** `/api/remessas`
- Body:
```json
{
  "remetenteId": "123e4567-e89b-12d3-a456-426614174000",
  "destinatarioId": "123e4567-e89b-12d3-a456-426614174001",
  "valorReais": 1000.0
}
```
- Exemplo de Resposta:
```json
{
  "remetenteId": "123e4567-e89b-12d3-a456-426614174000",
  "destinatarioId": "123e4567-e89b-12d3-a456-426614174001",
  "valorConvertidoDolares": 200.0
}
```

## Observações
- A cotação do dólar é ajustada para o último dia útil em finais de semana.
- Limites diários:
  - PF: R$ 10.000,00
  - PJ: R$ 50.000,00
- Operações são transacionais e reversíveis em caso de falha.

## Contato
Para dúvidas ou sugestões, entre em contato pelo e-mail: suporte@testeinter.com.
