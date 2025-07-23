# 🔁 Reactive WebFlux Aggregation Service

## 🚀 Overview
This project is a modern **Spring WebFlux** API Aggregator, designed for high concurrency, non-blocking I/O, and robust error handling. It fetches and aggregates data from third-party APIs, with a focus on clean architecture, observability, and extensibility.

---

## ⚙️ Architecture

```
Client
   │
   ▼
[Controller]  <--- OpenAPI/Swagger, Validation, Standardized Responses
   │
   ▼
[Service]     <--- Business Logic, Concurrency, Error Propagation
   │
   ▼
[ThirdPartyService] <--- API Calls, Retry, Timeout, Logging
   │
   ▼
[BaseApiService]   <--- WebClient, Resilience, Context Propagation
   │
   ▼
[WebClient]   <--- Non-blocking HTTP
```

- **Controller**: Annotated with OpenAPI for documentation, uses `@Valid` for validation, returns standardized `BaseResponse`.
- **Service**: Handles business logic, concurrency, and error propagation.
- **ThirdPartyService**: Encapsulates third-party API calls, retry, and timeout logic.
- **BaseApiService**: Centralizes WebClient configuration, error handling, and logging.
- **GlobalExceptionHandler**: Handles all errors, returns rich error payloads.

---

## 📁 Project Structure

```
src/main/java/com/nexa/rick_fraud/orchestrator/
├── annotation/         # Logging/Tracing Annotations
├── config/             # WebClient, Logging, OpenAPI Config
├── constant/           # Constants and Enums
├── controller/         # REST Controllers (with OpenAPI)
├── dto/                # Data Transfer Objects (with Validation)
├── exception/          # Custom Exceptions & Global Handler
├── service/            # Business Logic Services
├── third_party/        # Third-party API Integration
├── utils/              # Utility Classes (Response, Error, Log, JSON)
└── ReactiveApplication.java
```

---

## 📝 Features & Best Practices

- **Spring WebFlux**: Fully reactive, non-blocking stack.
- **OpenAPI/Swagger**: Auto-generated API docs at `/swagger-ui.html`.
- **Validation**: DTOs use `@NotNull`, `@Size`, etc. Controllers use `@Valid`.
- **Standardized Responses**: All endpoints return `BaseResponse<T>` with rich `meta` info.
- **Centralized Error Handling**: All errors handled by `GlobalExceptionHandler`, with detailed error payloads.
- **Configurable Concurrency**: Service concurrency is set via `application.yml`.
- **Observability**: Structured logging with requestId, ready for integration with tracing/metrics.
- **Test Coverage**: Unit and integration tests for utilities and endpoints.

---

## 🔧 Configuration Example (`application.yml`)

```yaml
service:
  concurrency: 10
third-party:
  apis:
    api1:
      url: https://jsonplaceholder.typicode.com
      timeout: 5000
      max-connections: 100
      retry-max-attempts: 3
      retry-delay-ms: 500
```

---

## 🧑‍💻 Development & Usage

### 1. Run the Project
```bash
./mvnw spring-boot:run
```

### 2. Access API Docs
- Visit: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Example API Call
```bash
curl http://localhost:8080/api/data
```

---

## 🛡️ Extending the Project
- Add new endpoints by creating DTOs (with validation), service methods, and controller methods.
- Annotate endpoints and DTOs with OpenAPI annotations for documentation.
- Use `ResponseUtils`, `ErrorUtils`, and `LogUtils` for consistency.
- Add integration/unit tests for new features.

---

## 📚 References
- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Project Reactor](https://projectreactor.io/)

