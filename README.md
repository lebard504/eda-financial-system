## Architecture

The system is built following a combination of **Event-Driven Architecture (EDA)**, **Domain-Driven Design (DDD)**, and **Clean Architecture**, enabling scalability, resilience, and clear separation of responsibilities.

---

### Event-Driven Architecture (EDA)

The system leverages **Apache Kafka** as the messaging backbone to enable asynchronous communication between services.

#### Key Concepts

- Services act as **event producers**, emitting domain events when state changes occur
- Other services act as **event consumers**, reacting independently
- Communication is **asynchronous**, avoiding tight coupling between services
- The system embraces **eventual consistency** across bounded contexts

#### Message Delivery Guarantees

The system relies on Kafka delivery semantics:

- At-least-once delivery (default)
- Consumers must be idempotent to handle duplicate events

#### Event Flow

- `customer-service` publishes `CustomerCreatedEvent`
- `account-service` consumes the event and creates a local customer snapshot
- `account-service` publishes `TransactionCreatedEvent`
- Downstream systems (future consumers) can react (fraud detection, analytics, etc.)


#### Event Consumption Model

Event consumers are implemented using Kafka listeners within each service.

- Consumers subscribe to specific topics
- Events are deserialized into domain events
- Application services handle the business reaction
- Data is persisted as part of local transactions

Example:

- account-service subscribes to `CustomerCreatedEvent`
- A consumer receives the event
- The event is mapped into a domain model (CustomerSnapshot)
- The snapshot is stored locally for consistency

#### Consumer Responsibilities

- Maintain local read models (event-driven replication)
- Trigger business workflows
- Ensure idempotent processing (avoid duplicates)

#### Benefits

- Loose coupling between services
- High extensibility (new consumers can be added without modifying producers)
- Fault isolation between domains
- Reactive and scalable workflows

---

### Domain-Driven Design (DDD)

Each microservice is modeled as an independent **bounded context**, encapsulating its own domain logic and data.

#### Bounded Contexts

- `customer-service` → Customer domain
- `account-service` → Account and Transaction domain

#### Domain Structure

Each service includes:

- **Aggregates** (e.g., Account as consistency boundary)
- **Entities** (Customer, Account, Transaction)
- **Value Objects** (e.g., balance, identifiers)
- **Domain Services** (business logic that does not belong to a single entity)
- **Repository Interfaces (Ports)** to abstract persistence

#### Key Principles

- Domain logic is isolated from infrastructure concerns
- Each service owns its data (no shared database)
- Cross-service communication is handled via events, not direct calls

#### Data Ownership

- Each microservice owns its own database
- No direct database sharing between services
- Data synchronization is achieved through events

This ensures:

- Strong encapsulation of domain logic
- Independent scalability
- Reduced coupling between services

---

### Clean Architecture

The system enforces a layered architecture to separate concerns and ensure maintainability.


#### Layers

- **domain**
  - Contains core business rules and models
  - Independent of frameworks and external systems

- **application**
  - Implements use cases and orchestrates domain operations
  - Handles transaction boundaries and business workflows

- **infrastructure**
  - Provides implementations for external concerns:
    - Database (JPA)
    - Messaging (Kafka producers/consumers)
  - Acts as adapters for external systems

- **interfaces**
  - Entry points (REST controllers)
  - Handles HTTP requests and responses

#### Architectural Patterns Applied

- Ports and Adapters (Hexagonal Architecture)
- Repository Pattern (domain-driven persistence abstraction)
- CQRS-inspired separation (commands vs queries)
- Event-driven communication instead of synchronous coupling

#### Anti-Corruption Layer (ACL)

When consuming external events, services transform incoming data into their own domain model.

Example:
- CustomerCreatedEvent is transformed into a local CustomerSnapshot
- Prevents external models from leaking into the domain

This ensures:
- Domain isolation
- Independence from external service changes

#### Benefits

- Framework independence (domain is not tied to Spring)
- High testability (business logic can be tested in isolation)
- Clear separation of responsibilities
- Easier scalability and evolution of each layer

---

## Resilience Patterns

The system implements multiple resilience strategies to ensure stability and fault tolerance in a distributed environment:

### 1. Idempotency
Transactions are protected using an idempotency key to prevent duplicate processing.

### 2. Circuit Breaker (Resilience4j)
Critical operations are protected using circuit breakers to prevent cascading failures.

Example:
- Transaction creation uses CircuitBreaker + Retry annotations
- Fallback mechanism returns controlled error when system is degraded

### 3. Retry Mechanism
Transient failures are handled using automatic retries with Resilience4j.

### 4. Fallback Handling
Fallback methods are implemented to gracefully degrade functionality when dependencies fail.

### 5. Event-Driven Decoupling
Services communicate asynchronously via Kafka, reducing tight coupling and improving fault isolation.

### 6. Fault Isolation
Each microservice operates independently, preventing failures from propagating across domains.

### 7. Eventual Consistency
The system embraces eventual consistency through asynchronous event propagation.

### 8. Transaction Boundary Control
Local transactions are managed within each service using database transactions, avoiding distributed locking.

---

## Technologies

### Core Backend
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring AOP

### Architecture and Design
- Event-Driven Architecture (EDA)
- Domain-Driven Design (DDD)
- Clean Architecture
- Repository Pattern
- Ports and Adapters
- Resilience4j

### Messaging
- Apache Kafka
- Apache Zookeeper

### Database
- H2 Database
- Spring Data JPA
- Hibernate / Jakarta Persistence

### Build and Dependency Management
- Maven

### Testing
- JUnit 5
- Mockito
- Spring Boot Test

### Containerization and Local Orchestration
- Docker
- Docker Compose

### API and Documentation
- REST APIs
- Postman Collection

### Utilities and Developer Experience
- Lombok

---

## Project Structure

```
eda-financial-system/
│
├── account-service/
│   ├── src/main/java/com/devsu/financial/account_service/
│   │   ├── application/
│   │   │   ├── services/
│   │   │   └── queries/
│   │   │       └── dto/
│   │   │
│   │   ├── config/
│   │   │
│   │   ├── domain/
│   │   │   ├── models/
│   │   │   └── repositories/
│   │   │
│   │   ├── exceptions/
│   │   │
│   │   ├── handler/
│   │   │
│   │   ├── infrastructure/
│   │   │   ├── entities/
│   │   │   ├── messaging/
│   │   │   │   ├── consumers/
│   │   │   │   ├── producers/
│   │   │   │   └── events/
│   │   │   └── persistence/
│   │   │       ├── adapters/
│   │   │       └── jpa/
│   │   │
│   │   ├── interfaces/
│   │   │   └── controllers/
│   │   │
│   │   ├── shared/
│   │   │   └── utils/
│   │   │
│   │   └── AccountServiceApplication.java
│   │
│   ├── resources/
│   └── test/
```

| Folder / Path                                      | Description                                                                 |
|----------------------------------------------------|-----------------------------------------------------------------------------|
| application/                                       | Use cases and business orchestration layer                                 |
| application/services/                              | Core business logic (e.g., TransactionService)                             |
| application/queries/                               | Query logic separated from commands (CQRS style)                           |
| application/queries/dto/                           | DTOs for query responses                                                   |
| config/                                            | Spring Boot configurations (beans, Kafka config, etc.)                     |
| domain/                                            | Core domain layer (pure business rules)                                    |
| domain/models/                                     | Entities and aggregates (Account, Transaction)                             |
| domain/repositories/                               | Repository interfaces (ports)                                              |
| exceptions/                                        | Custom exceptions for business rules                                       |
| handler/                                           | Global exception handlers (ControllerAdvice)                               |
| infrastructure/                                    | External implementations and adapters                                      |
| infrastructure/entities/                           | JPA entities mapped to database                                            |
| infrastructure/messaging/                          | Event-driven layer (Kafka integration)                                     |
| infrastructure/messaging/consumers/                | Kafka consumers (event listeners)                                          |
| infrastructure/messaging/producers/                | Kafka producers (event publishers)                                         |
| infrastructure/messaging/events/                   | Event definitions (TransactionCreatedEvent, etc.)                          |
| infrastructure/persistence/                        | Persistence adapters                                                       |
| infrastructure/persistence/adapters/               | Repository implementations (bridge domain ↔ DB)                            |
| infrastructure/persistence/jpa/                    | Spring Data JPA repositories                                               |
| interfaces/                                        | Entry points of the system                                                 |
| interfaces/controllers/                            | REST controllers                                                           |
| shared/                                            | Shared utilities and helpers                                               |
| shared/utils/                                      | Utility classes                                                            |
| AccountServiceApplication.java                     | Main Spring Boot entry point                                               |
---

## How to Run

### Requirements

- Docker
- Docker Compose

---

### Start System

```
docker-compose up --build
```

---

## Services

| Service           | URL                      |
|------------------|--------------------------|
| account-service  | http://localhost:8081    |
| customer-service | http://localhost:8082    |
| kafka-ui         | http://localhost:8083    |

---

## Docker Architecture

The system includes:

- Zookeeper
- Kafka
- Kafka UI
- account-service
- customer-service

All services run in the same Docker network.

---
## Database

The system uses an **H2 in-memory database** for local development and testing.

### Characteristics

- In-memory database (non-persistent)
- Data is lost on application restart
- Zero external dependencies
- Fast startup and lightweight

### Initialization (Seed Data)

At application startup, the system automatically loads **seed data** into the database.

This includes:

- Customers
- Accounts
- Initial relationships between entities

This allows the system to be immediately usable without manual data setup.

### Notes

- The database is ephemeral and intended only for development/testing
- Every restart resets the state to the initial seeded data
- No manual setup is required to start using the system

---

## Environment Variables and Configuration

### Application (account-service)

```
SPRING_PROFILES_ACTIVE=docker
CREATE_DEFAULT_ACCOUNT=true
JAVA_TOOL_OPTIONS=-Dspring.h2.console.settings.web-allow-others=true
```

#### Purpose

This variable enables automatic creation of a default account during application startup.

#### Behavior

- When enabled (`true`):
  - The system creates a default account at startup if it does not exist
  - Useful for local development, testing, and demo environments

- When disabled (`false` or not set):
  - No automatic data initialization occurs
  - Recommended for production environments

#### Architectural Pattern

This mechanism follows a **Bootstrap / Seed Pattern**, allowing controlled initialization of domain data without affecting business logic.

#### Benefits

- Avoids manual setup for testing
- Ensures consistent initial state
- Keeps initialization logic outside core domain rules

### Kafka Infrastructure (Docker Compose)

```
KAFKA_BROKER_ID=1
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
```

### Zookeeper

```
ZOOKEEPER_CLIENT_PORT=2181
```

### Kafka UI

```
KAFKA_CLUSTERS_0_NAME=local
KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9092
```
---

## Use Cases

### Customer Service (customer-service)

#### Commands
- Create customer
- Update customer
- Delete customer

#### Queries
- Get customer by ID
- Get all customers

#### Events Produced
- CustomerCreatedEvent

#### Events Consumed
- None (source of truth for customer domain)

---

### Account Service (account-service)

#### Commands
- Create account
- Update account
- Delete account

#### Queries
- Get account by ID
- Get all accounts

#### Business Rules
- Account must be linked to an existing customer
- Balance initialization rules
- Account state validation

#### Events Produced
- TransactionEventProducer

#### Events Consumed
- CustomerCreatedEvent (to build local customer snapshot)

---

### Transactions (account-service)

#### Commands
- Create deposit transaction
- Create withdrawal transaction

#### Queries
- Get transaction by ID
- Get all transactions

#### Business Rules
- Idempotency validation using idempotencyKey
- Balance validation before withdrawal
- Automatic balance recalculation
- Transaction timestamp generation

#### Events Produced
- TransactionCreatedEvent

#### Events Consumed
- None (acts as event producer for downstream systems)

---

### Reports (account-service)

#### Queries
- Generate account statement report
- Filter transactions by:
  - date
  - client
  - account

#### Data Sources
- Transactions
- Account data
- Customer snapshot data

#### Characteristics
- Read-only operations
- Optimized queries (CQRS-style separation)

---

### Event-Driven Flows (EDA)

#### Customer → Account synchronization
1. Customer is created in customer-service
2. CustomerCreatedEvent is published to Kafka
3. account-service consumes the event
4. A local CustomerSnapshot is stored

#### Transaction processing flow
1. Transaction request is received
2. Business validations are executed (idempotency, balance)
3. Transaction is persisted
4. TransactionCreatedEvent is published
5. Downstream consumers can react (fraud detection, analytics, etc.)

---

### System Characteristics

- Strong consistency inside each service (local transactions)
- Eventual consistency between services
- Loose coupling via Kafka events
- Clear separation between commands and queries (CQRS-inspired)
- High extensibility for new consumers without modifying core services

---

## Testing

Run tests:

```
mvn clean test
```

Types:
- Unit tests (Mockito)
- Integration tests (service layer)

---

## API Testing

A Postman collection is included to validate all available endpoints and use cases.

### Location

```
/postman
```

### Required Setup

Before executing requests, you must configure the following environment variables in Postman:

| Variable              | Description                           | Example                     |
|----------------------|---------------------------------------|-----------------------------|
| customerServiceUrl   | Base URL for customer-service         | http://localhost:8082       |
| accountServiceUrl    | Base URL for account-service          | http://localhost:8081       |
| clientId             | Existing customer identifier          | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2                         |
| accountId            | Existing account identifier           | 22222222-2222-2222-2222-222222222222                        |

### Notes

- The collection is designed to work with the system running via Docker Compose
- Some requests depend on previously created resources (e.g., account requires a valid clientId)
- The flow is intended to simulate real business operations:
  - Create customer → Create account → Execute transactions → Generate reports

---

## Design Goals

- High scalability through distributed microservices
- Loose coupling via event-driven communication (Kafka)
- Clear domain boundaries using Domain-Driven Design (DDD)
- Separation of concerns with Clean Architecture
- Resilient and fault-tolerant system design
- Extensible architecture for future event consumers
- Production-ready structure aligned with real-world systems

---

## Future Improvements

- Saga Pattern for distributed transaction orchestration
- Observability stack (Prometheus + Grafana + tracing)
- API Gateway (centralized routing, auth, throttling)
- Authentication & Authorization (JWT / Keycloak)
- Distributed caching (Redis)
- Dead Letter Queues (DLQ) for Kafka error handling
- Schema Registry for event versioning
- CI/CD pipelines with automated testing and deployment
