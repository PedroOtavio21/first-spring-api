# Spring Boot — API de básica Produtos

Projeto de estudo de uma REST API construída com **Java 17 + Spring Boot**, sem banco de dados. O objetivo é fixar a arquitetura em camadas, injeção de dependência, tratamento de erros e boas práticas visto em outras linguagens no Java com Spring.

---

## Arquitetura em camadas

Cada camada tem uma responsabilidade única e só se comunica com a imediatamente abaixo.

```
┌──────────────────────────────────────┐
│        HTTP Client (Postman, etc.)   │
└──────────────────┬───────────────────┘
                   │ HTTP Request
┌──────────────────▼───────────────────┐
│    Controller — camada HTTP          │
│  Recebe a requisição, delega ao      │
│  Service e define o status HTTP      │
└──────────────────┬───────────────────┘
                   │
┌──────────────────▼───────────────────┐
│    Service — regras de negócio       │
│  Contém toda a lógica da aplicação.  │
│  Não sabe nada sobre HTTP.           │
└──────────────────┬───────────────────┘
                   │
┌──────────────────▼───────────────────┐
│    Model — dados                     │
│  Representa o produto. Aqui é uma   │
│  List em memória; futuramente, JPA. │
└──────────────────────────────────────┘
```

---

## Principais arquivos

### `model/Product.java`
Representa um produto no sistema. Circula internamente entre as camadas. Usa `BigDecimal` para o preço — obrigatório para valores monetários, pois `double` e `float` têm erros de precisão.

### `dto/ProductRequest.java`
Payload recebido pelo cliente nas operações de criação e atualização. Não contém `id` porque quem gera o identificador é o servidor, nunca o cliente.

### `service/ProductService.java`
Contém toda a lógica do CRUD: geração de ID, busca, atualização e remoção na lista em memória. Lança `NotFoundException` quando um produto não é encontrado.

### `controller/ProductController.java`
Expõe os endpoints REST. Delega toda lógica ao `ProductService` — sua única responsabilidade é mapear HTTP para chamadas de serviço e declarar o status de resposta.

| Método | Path | Status |
|---|---|---|
| `GET` | `/v1/products` | 200 OK |
| `POST` | `/v1/products` | 201 Created |
| `PUT` | `/v1/products/{id}` | 200 OK |
| `DELETE` | `/v1/products/{id}` | 204 No Content |

### `exception/` e `handler/GlobalExceptionHandler.java`
`NotFoundException` é lançada pelo Service quando um produto não existe. `GlobalExceptionHandler` a intercepta e retorna uma resposta padronizada ao cliente — sem que o Controller precise de nenhum `try/catch`.

```json
{ "message": "Product not found with id: 99", "status": 404 }
```

---

## Fluxo de uma requisição

**Sucesso — POST /v1/products**
```
Cliente envia JSON → Controller deserializa para ProductRequest
→ Service gera ID, cria Product e adiciona na lista
→ Controller retorna Product serializado com status 201
```

**Erro — PUT /v1/products/99 (ID inexistente)**
```
Service não encontra o produto → lança NotFoundException
→ GlobalExceptionHandler intercepta
→ Cliente recebe { "message": "...", "status": 404 }
```

---

## Como executar

**Pré-requisitos:** Java 17+ e Maven 3.x instalados.

```bash
git clone <url-do-repo>
cd spring-boot-project

./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.
Swagger UI disponível em `http://localhost:8080/swagger-ui.html`.