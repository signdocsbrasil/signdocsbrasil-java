# signdocsbrasil-api (Java)

SDK oficial em Java para a API SignDocsBrasil.

## Requisitos

- Java 11+
- Dependência: Gson 2.11+

## Instalação

### Maven

```xml
<dependency>
    <groupId>com.signdocsbrasil</groupId>
    <artifactId>signdocsbrasil-api</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.signdocsbrasil:signdocsbrasil-api:1.1.0'
```

## Início Rápido

```java
import com.signdocsbrasil.api.SignDocsBrasilClient;
import com.signdocsbrasil.api.models.*;

SignDocsBrasilClient client = SignDocsBrasilClient.builder()
    .clientId("seu_client_id")
    .clientSecret("seu_client_secret")
    .build();

CreateTransactionRequest request = new CreateTransactionRequest();
request.purpose = "DOCUMENT_SIGNATURE";
request.policy = new Policy("CLICK_ONLY");
request.signer = new Signer("João Silva", "joao@example.com", "user-001");
request.document = new CreateTransactionRequest.InlineDocument(pdfBase64, "contrato.pdf");

Transaction tx = client.transactions().create(request);
System.out.println(tx.transactionId + " " + tx.status);
```

### Private Key JWT (ES256)

```java
String keyPem = Files.readString(Path.of("./private-key.pem"));

SignDocsBrasilClient client = SignDocsBrasilClient.builder()
    .clientId("seu_client_id")
    .privateKey(keyPem)
    .kid("seu-key-id")
    .build();
```

## Recursos Disponíveis

| Recurso | Métodos |
|---------|---------|
| `client.transactions()` | `create`, `list`, `get`, `cancel`, `finalize`, `listAutoPaginate` |
| `client.documents()` | `upload`, `presign`, `confirm`, `download` |
| `client.steps()` | `list`, `start`, `complete` |
| `client.signing()` | `prepare`, `complete` |
| `client.evidence()` | `get` |
| `client.verification()` | `verify`, `downloads` |
| `client.users()` | `enroll` |
| `client.webhooks()` | `register`, `list`, `delete`, `test` |
| `client.signingSessions()` | `create`, `getStatus`, `cancel`, `list`, `waitForCompletion` |
| `client.envelopes()` | `create`, `get`, `addSession`, `combinedStamp` |
| `client.documentGroups()` | `combinedStamp` |
| `client.health()` | `check`, `history` |

## Envelopes (Múltiplos Signatários)

```java
CreateEnvelopeRequest envRequest = new CreateEnvelopeRequest();
envRequest.setSigningMode("PARALLEL");
envRequest.setTotalSigners(2);
envRequest.setDocumentContent(pdfBase64);
envRequest.setDocumentFilename("contrato.pdf");

Envelope envelope = client.envelopes().create(envRequest);

AddEnvelopeSessionRequest session1Req = new AddEnvelopeSessionRequest();
session1Req.setSignerName("João Silva");
session1Req.setSignerEmail("joao@example.com");
session1Req.setPolicyProfile("CLICK_ONLY");
session1Req.setSignerIndex(1);

EnvelopeSession session1 = client.envelopes().addSession(envelope.getEnvelopeId(), session1Req);

AddEnvelopeSessionRequest session2Req = new AddEnvelopeSessionRequest();
session2Req.setSignerName("Maria Santos");
session2Req.setSignerEmail("maria@example.com");
session2Req.setPolicyProfile("CLICK_ONLY");
session2Req.setSignerIndex(2);

EnvelopeSession session2 = client.envelopes().addSession(envelope.getEnvelopeId(), session2Req);

System.out.println(session1.getUrl() + " " + session2.getUrl());
```

## Configuração Avançada

### HTTP Client customizado

Injete um `java.net.http.HttpClient` customizado (ex: para proxying ou SSL customizado):

```java
import java.net.http.HttpClient;

HttpClient httpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build();

SignDocsBrasilClient client = SignDocsBrasilClient.builder()
    .clientId("seu_client_id")
    .clientSecret("seu_client_secret")
    .httpClient(httpClient)
    .build();
```

### Logging

O SDK aceita um `java.util.logging.Logger`. São logados apenas: método HTTP, path, status code e duração. Headers de autorização, corpos de request/response e tokens nunca são logados.

```java
import java.util.logging.Logger;

Logger logger = Logger.getLogger("signdocs");

SignDocsBrasilClient client = SignDocsBrasilClient.builder()
    .clientId("seu_client_id")
    .clientSecret("seu_client_secret")
    .logger(logger)
    .build();
```

Para usar com SLF4J, configure a bridge `jul-to-slf4j`.

### Timeout por requisição

Todas as operações possuem sobrecarga com `Duration timeout`, que sobrescreve o timeout padrão do client:

```java
Transaction tx = client.transactions().get("tx_123", Duration.ofSeconds(5));
```

## Documentação

Para guias completos de integração com exemplos passo-a-passo de todos os fluxos de assinatura, veja a [documentação centralizada](../docs/README.md).
