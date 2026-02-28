## Java21-VirtualThread-CryptoEngine

A Spring Boot background service that periodically fetches crypto prices from Binance, stores them in PostgreSQL, and triggers alerts based on thresholds you define. It uses Java 21 Virtual Threads, `@Scheduled` jobs, Spring Web `RestClient`, Spring Data JPA, and Docker Compose.

### Features
- **Periodic price polling**: Fetches prices for popular symbols every second via `@Scheduled`.
- **Concurrent and scalable**: Java 21 **Virtual Threads** for parallel symbol fetching.
- **Binance integration**: Live prices from `https://api.binance.com/api/v3/ticker/price` via `RestClient`.
- **Persistent storage**: Prices are written to the `crypto_price` table via JPA.
- **Alerting**: Triggers alerts (ABOVE/BELOW) based on rules in the `alert` table.
- **Asynchronous notification**: `NotificationService` (`@Async`) logs a sample notification.
- **Ready-to-run database**: Local PostgreSQL via `docker-compose.yml`.

### Architecture Overview
- `CryptoBackgroundService`: Scheduler that kicks off periodic fetching.
- `CryptoService`: Calls Binance, checks alerts, and persists prices.
- `AlertService`: Evaluates `alert` records and triggers matching rules.
- `NotificationService`: Sends triggered alerts asynchronously (currently logs).
- `entity`, `repository`, `dto`, `enums`: JPA entities, repositories, transfer objects, and enums.


## Setup

### Requirements
- Java 21 (JDK)
- Maven 3.9+
- Docker (optional; recommended for PostgreSQL)

### Start the database (Docker)
```bash
docker compose up -d
```

PostgreSQL starts on `localhost:5432` with database `cryptodb` (user/password: `devuser` / `devpassword`). These match `application.yaml`.


## Run

### With Maven
```bash
mvn spring-boot:run
```

### As a JAR
```bash
mvn clean package
java -jar target/crypto-watcher-0.0.1-SNAPSHOT.jar
```

About ~5 seconds after startup, the scheduler begins and fetches prices every second.


## Configuration

Configured via `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cryptodb
    username: devuser
    password: devpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  threads:
    virtual:
      enabled: true
```

You can override with environment variables:

- PowerShell (Windows):
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/cryptodb"
$env:SPRING_DATASOURCE_USERNAME="devuser"
$env:SPRING_DATASOURCE_PASSWORD="devpassword"
mvn spring-boot:run
```

- Bash:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/cryptodb \
SPRING_DATASOURCE_USERNAME=devuser \
SPRING_DATASOURCE_PASSWORD=devpassword \
mvn spring-boot:run
```


## Symbol List
By default, the following symbols are polled: `BTCUSDT, ETHUSDT, BNBUSDT, ADAUSDT, SOLUSDT, DOGEUSDT, XRPUSDT, DOTUSDT, MATICUSDT, LTCUSDT`.

To change them, edit the `symbols` list inside `CryptoService.runLiveAnalysis()`.


## Alerts
For now, alerts are inserted manually into the database. Example (via psql):

```sql
INSERT INTO alert (symbol, target_price, condition, is_triggered, user_email)
VALUES ('BTCUSDT', 65000, 'ABOVE', false, 'user@example.com');
```

- Valid `condition` values: `ABOVE` or `BELOW`.
- After an alert is triggered, it is marked with `is_triggered = true`.


## REST API

Base URL: `http://localhost:8080`

- **GET** `/api/v1/prices/{symbol}`
  - Returns the last known price for a symbol.
  - Example:

```bash
curl -s http://localhost:8080/api/v1/prices/BTCUSDT
```

Example response:

```json
{
  "symbol": "BTCUSDT",
  "targetPrice": 65000.12
}
```

- **GET** `/api/v1/alerts`
  - Lists all non-triggered (active) alerts.
  - Example:

```bash
curl -s http://localhost:8080/api/v1/alerts
```

Example response:

```json
[
  {
    "id": 1,
    "symbol": "BTCUSDT",
    "targetPrice": 65000,
    "condition": "ABOVE",
    "triggered": false,
    "userEmail": "user@example.com"
  }
]
```

- **POST** `/api/v1/alerts`
  - Creates a new alert.
  - Body (JSON):

```json
{
  "symbol": "BTCUSDT",
  "targetPrice": 65000,
  "condition": "ABOVE",
  "userEmail": "user@example.com"
}
```

  - Example:

```bash
curl -s -X POST http://localhost:8080/api/v1/alerts \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "BTCUSDT",
    "targetPrice": 65000,
    "condition": "ABOVE",
    "userEmail": "user@example.com"
  }'
```

  - Notes:
    - `condition` accepts `ABOVE` or `BELOW`.
    - Validation errors return `400 Bad Request` with details.

## Logs and Observability
The app logs each periodic trigger, total batch fetch duration, and triggered alerts. Sample (messages are localized in code):

```text
>>> Periyodik veri çekme işlemi başladı: 2026-02-28T12:34:56.789
Canlı veri çekme tamamlandı. Toplam süre: 123 ms
🔔 [ALARM GÖNDERİLDİ] -> Alıcı: user@example.com, Coin: BTCUSDT, Tetiklenen Fiyat: 65000
```


## Tech Stack
- Java 21 (Virtual Threads enabled)
- Spring Boot 3.5.x
- Spring Web (`RestClient`)
- Spring Data JPA (Hibernate)
- PostgreSQL (Docker Compose)


## Roadmap / Improvements
- Real notification channels (email/SMS/Telegram integrations)
- REST API for alert create/list/delete
- More alert conditions (e.g., percent change)
- Increased test coverage


## Project Structure (Short)
```text
src/main/java/com/crypto/watcher/crypto_watcher/
 ├─ service/
 │   ├─ CryptoBackgroundService.java
 │   ├─ CryptoService.java
 │   ├─ AlertService.java
 │   └─ NotificationService.java
 ├─ entity/
 │   ├─ CryptoPrice.java
 │   └─ Alert.java
 ├─ repository/
 │   ├─ PriceRepository.java
 │   └─ AlertRepository.java
 ├─ dto/
 │   └─ BinancePriceRecord.java
 └─ enums/
     └─ AlertCondition.java
```


## License
No license specified. If you want to open-source, add a `LICENSE` file (e.g., MIT).
