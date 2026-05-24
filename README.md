# 🐇 OnlyBuns - Backend

> **Already familiar with OnlyBuns?** If you've read the [Frontend README](https://github.com/HakTak/onlybuns-social-platform-frontend), jump directly to [Backend Architecture & Technologies](#backend-architecture--technologies).

---

## 📱 Project Overview

### OnlyBuns: High-Concurrency Social Platform | 2024

**Repository Links:** [Frontend](https://github.com/HakTak/onlybuns-social-platform-frontend) · [Backend](https://github.com/HakTak/onlybuns-social-platform-backend)

**Tech Stack:** `Java` `Spring Boot` `Angular` `PostgreSQL` `MongoDB` `RabbitMQ` `WebSockets` `Prometheus` `Grafana` `Docker`

OnlyBuns is a feature-complete social network built around **real distributed systems challenges**:

- **Bloom Filter** for O(1) username collision detection
- Database-level transaction handling for concurrent likes and follows
- Custom **rate limiter** and **load balancer** implemented from scratch
- Real-time group chat via WebSockets
- Location-based post discovery on interactive maps
- Fanout/direct message queues with RabbitMQ
- Full observability stack with Prometheus and Grafana

*Group project developed by 3 students.*

---

## Backend Architecture & Technologies

### Core Stack
- **Framework:** Spring Boot 2.1.9 with Spring Security & OAuth2
- **Primary DB:** PostgreSQL (transactional data, consistency)
- **Secondary DB:** MongoDB (flexible schemas, analytics)
- **Message Queue:** RabbitMQ (async event processing, fanout messaging)
- **Real-time Communication:** WebSockets (Spring Boot WebSocket support)
- **Authentication:** JWT tokens
- **Rate Limiting:** Custom rate limiter + Bucket4J
- **Observability:** Prometheus metrics integration

### Key Features

#### 🔐 Distributed System Optimizations
- **Bloom Filter Implementation** - O(1) username availability checks with minimal false positives
- **Optimistic Locking & Transactions** - Database-level handling of concurrent operations (likes, follows)
- **Custom Rate Limiter** - Token bucket algorithm for request throttling
- **Load Balancer** - Python-based load balancer (separate module) for request distribution

#### 💬 Real-Time Features
- **WebSocket Integration** - Live group chat and notifications
- **RabbitMQ Messaging** - Event-driven architecture for likes, follows, comments
- **Message Queues** - Fanout for broadcast events, direct queues for targeted messaging

#### 📊 Analytics & Observability
- **Prometheus Metrics** - Custom metrics for performance tracking
- **Scheduled Tasks** - Background jobs for analytics aggregation
- **Analytics Service** - User behavior tracking and trending content

#### 🗺️ Advanced Capabilities
- **Location-Based Services** - Geographic post filtering and discovery
- **OAuth2 & Security** - Secure authentication and authorization
- **Email Service** - Transactional emails (notifications, verification)

### Project Structure

```
isa-backend/
├── src/main/java/rs/ac/uns/ftn/informatika/jpa/
│   ├── controller/           # REST API endpoints
│   ├── service/              # Business logic layer
│   │   ├── UserService       # User management & auth
│   │   ├── PostService       # Post CRUD & interactions
│   │   ├── FollowService     # Follow relationships & transactions
│   │   ├── ChatService       # WebSocket chat handling
│   │   ├── RateLimiterService# Custom rate limiting
│   │   ├── BunnyProducerService    # RabbitMQ event publishing
│   │   ├── BunnyConsumerService    # RabbitMQ event consumption
│   │   └── AnalyticsService  # Analytics & aggregation
│   ├── entity/               # JPA entities (PostgreSQL)
│   ├── repository/           # Data access layer
│   ├── security/             # JWT, OAuth2 configuration
│   ├── interceptor/          # Rate limiting interceptor
│   └── scheduled/            # Scheduled background tasks
├── pom.xml                   # Maven dependencies
└── storage/                  # File uploads storage
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 8+**
- **Maven 3.6+**
- **PostgreSQL 12+**
- **MongoDB 4.4+**
- **RabbitMQ 3.8+**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/HakTak/onlybuns-social-platform-backend.git
   cd isa-backend
   ```

2. **Set up databases**
   ```bash
   # PostgreSQL - create database
   createdb onlybuns
   
   # MongoDB - ensure service is running
   mongod --dbpath /path/to/mongodb/data
   
   # RabbitMQ - ensure service is running
   # Default: localhost:5672, User: guest, Pass: guest
   ```

3. **Configure application properties**
   ```bash
   # Edit src/main/resources/application.properties
   # Update database credentials and connection strings
   spring.datasource.url=jdbc:postgresql://localhost:5432/onlybuns
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   
   spring.data.mongodb.uri=mongodb://localhost:27017/onlybuns
   
   spring.rabbitmq.host=localhost
   spring.rabbitmq.port=5672
   spring.rabbitmq.username=guest
   spring.rabbitmq.password=guest
   ```

4. **Build and run**
   ```bash
   # Build
   mvn clean package
   
   # Run
   java -jar target/rest-example-0.0.1-SNAPSHOT.war
   # Or use Maven
   mvn spring-boot:run
   ```

5. **Access the API**
   - Base URL: `http://localhost:8080`
   - API Documentation: `http://localhost:8080/swagger-ui.html`

---

## 🏗️ Architecture Highlights

### Database Strategy
- **PostgreSQL:** Handles all transactional data with ACID compliance for concurrent operations
- **MongoDB:** Stores analytics data, audit logs, and flexible structures
- **Transactions:** Pessimistic/Optimistic locking for simultaneous likes and follows

### Message-Driven Architecture
```
User Action → RabbitMQ → Event Processing → Analytics/Cache Update
```
- Producer publishes events (BunnyProducerService)
- Consumer processes asynchronously (BunnyConsumerService)
- Decouples real-time responses from background processing

### Rate Limiting
- **Token Bucket Algorithm:** Configurable per-endpoint rate limits
- **Interceptor-based:** Applied transparently at HTTP level
- **Fallback:** Custom implementation ensures no external dependencies

### Security Layers
- JWT token-based authentication
- OAuth2 for third-party integrations
- Role-based access control (RBAC)
- XSS and CSRF protection

---

## 📊 Monitoring & Observability

### Prometheus Metrics
- Request latency and throughput
- Database connection pool stats
- RabbitMQ queue depth
- Custom application metrics

### Grafana Dashboards
- Real-time traffic visualization
- Performance tracking
- Error rate monitoring
- System resource utilization

### Access Monitoring
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000` (default: admin/admin)

---

## 🔄 CI/CD & Deployment

### Docker Support
- Dockerfile included for containerization
- Docker Compose configuration for full stack (PostgreSQL, MongoDB, RabbitMQ, Backend)

### Building Docker Image
```bash
docker build -t onlybuns-backend:latest .
docker run -p 8080:8080 --name onlybuns-backend onlybuns-backend:latest
```

---

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run with coverage
mvn test jacoco:report
```

---

## 📝 API Examples

### Authentication
```bash
# Register
POST /auth/register
Content-Type: application/json

{
  "username": "bunny123",
  "password": "secure_password",
  "email": "bunny@example.com"
}

# Login
POST /auth/login
{
  "username": "bunny123",
  "password": "secure_password"
}
```

### Posts
```bash
# Create post
POST /posts
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Hello OnlyBuns!",
  "content": "First post",
  "location": { "latitude": 45.26, "longitude": 19.84 }
}

# Get posts by location
GET /posts/location?lat=45.26&lon=19.84&radius=5
```

### Real-Time Chat
```
WebSocket: ws://localhost:8080/ws/chat?token={token}
Subscribe to: /topic/chat/{chatId}
Send to: /app/chat/message
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is part of an academic group assignment.

---

## 🔗 Links

- **Frontend Repository:** [onlybuns-social-platform-frontend](https://github.com/HakTak/onlybuns-social-platform-frontend)
- **API Documentation:** `http://localhost:8080/swagger-ui.html` (when running)
- **Prometheus:** `http://localhost:9090`
- **Grafana:** `http://localhost:3000`
