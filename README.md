# Order Payment Platform

A Spring Boot microservices project that demonstrates an **event-driven architecture** for order management and payment processing using **ActiveMQ** and the **Transactional Outbox pattern**.

---

## 🚀 Features
- **Order Service**: Handles order creation and persistence.
- **Payment Service**: Processes payments with idempotency guarantees.
- **Transactional Outbox**: Ensures reliable event publishing to ActiveMQ.
- **ActiveMQ Messaging**: Event-driven communication between services.
- **Retry & Dead Letter Queue (DLQ)**: Handles transient and permanent failures.
- **Docker Compose**: Containerized setup for services, DB, and broker.

---

## 📂 Project Structure
- `order-service/` → Order microservice
- `payment-service/` → Payment microservice
- `common-dto/` → Shared DTOs
- `docker-compose.yml` → Service orchestration
- `pom.xml` → Parent Maven configuration

---

## 🛠 Tech Stack
- Java 17
- Spring Boot
- ActiveMQ
- Hibernate / JPA
- MySQL
- Docker & Docker Compose

---

## ⚙️ How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/Sourav-Dwivedi/order-payment-platform.git
   cd order-payment-platform
