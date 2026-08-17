# Order and Payment Platform

## Overview
This project contains two microservices:
- **Order Service**: Manages order creation, cancellation, and event publishing.
- **Payment Service**: Handles payment processing, ensures idempotency, and publishes results.

## Features
- Event-driven architecture
- ActiveMQ messaging
- Transactional Outbox pattern
- Retry, Redelivery, Dead Letter Queue
- Dockerized deployment
- Automated tests

## Project Structure
- `orderservice/` → Order microservice
- `payment-service/` → Payment microservice
- `common-dto/` → Shared DTOs
- `docker-compose.yml` → Service orchestration
- `pom.xml` → Parent Maven configuration

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Sourav-Dwivedi/order-payment-platform.git
