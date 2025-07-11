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
  "remetenteId": "0bd2739d-e926-44ad-bdf5-49851410f627",
  "destinatarioId": "4bdf8485-13ac-4217-ba3f-6260527f4466",
  "valorReais": 50.0,
  "moeda": "dolares"
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

### 3. Adicionar Saldo à Carteira
**POST** `/api/usuarios/{id}/adicionar-saldo`

Este endpoint permite adicionar saldo em reais ou dólares à carteira de um usuário.

- **Parâmetros**:
  - `id` (path): ID do usuário.
  - `valor` (query): Valor a ser adicionado.
  - `moeda` (query): Moeda do saldo a ser adicionado (`reais` ou `dolares`).

- **Exemplo de Requisição**:
```bash
curl -X POST "http://localhost:8080/api/usuarios/{id}/adicionar-saldo?valor=100.0&moeda=reais"
```

- **Exemplo de Requisição em JSON**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "valor": 100.0,
  "moeda": "reais"
}
```

- **Respostas**:
  - `200 OK`: Saldo adicionado com sucesso.
  - `400 Bad Request`: Erro na requisição (ex.: moeda inválida).
  - `404 Not Found`: Usuário não encontrado.

### 4. Limites e Exceções

#### Limite Diário de Transações
O sistema possui um limite diário para transações de usuários do tipo Pessoa Física. Caso o limite seja excedido, uma exceção `LimiteDiarioExcedidoException` será lançada.

#### Moedas Suportadas
Atualmente, o sistema suporta apenas as moedas `reais` e `dolares`. Caso uma moeda inválida seja informada, uma exceção `IllegalArgumentException` será lançada com a mensagem "Moeda inválida. Use 'reais' ou 'dolares'."

## Observações
- A cotação do dólar é ajustada para o último dia útil em finais de semana.
- Limites diários:
  - PF: R$ 10.000,00
  - PJ: R$ 50.000,00
- Operações são transacionais e reversíveis em caso de falha.
