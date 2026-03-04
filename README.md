# 💰 Locked Term Based Investment Service

A backend service for managing locked-term savings products, built with Spring Boot and Domain-Driven Design (DDD).

---

## 🏗️ Architecture

The system is organized into **3 bounded contexts**:

| Bounded Context | Responsibility |
|---|---|
| `wallet` | User wallet management — balance, freeze/unfreeze funds |
| `saving` | Locked products, subscriptions, daily interest accrual, early redemption |
| `admin` | Liquidity pool management, fund injection, ledger tracking |

Each bounded context follows a layered structure:
```
api/          → Controllers, DTOs (request/response)
application/  → Services, CRON jobs
domain/       → Aggregates, Value Objects, Repositories (interfaces), Factories, Policies
infrastructure/ → JPA entities, Repository implementations
```

---

## ⚙️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.1 |
| ORM | Hibernate 7.2 / Spring Data JPA |
| Database | PostgreSQL (Supabase) |
| Connection Pool | HikariCP |
| Migration | Flyway 11 |
| ID Generation | ULID (ulid-creator) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |
| Hosting | Render |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker (for local PostgreSQL)
- Maven

### Run locally

**1. Start PostgreSQL via Docker:**
```bash
docker run --name postgres_locked_saving \
  -e POSTGRES_USER=saving \
  -e POSTGRES_PASSWORD=123 \
  -e POSTGRES_DB=locked_saving_db \
  -p 5432:5432 -d postgres:15
```

**2. Run the application with `local` profile:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Flyway will automatically create all tables on first run.

**3. Access Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

---

## 🗄️ Database Migration

This project uses **Flyway** for schema management.

| File | Description |
|---|---|
| `V1__init_schema.sql` | Initial schema — all tables, constraints, indexes |

To add a new migration, create `V2__description.sql` in:
```
src/main/resources/db/migration/
```

> ⚠️ Never modify existing migration files that have already been applied.

---

## 🔄 Daily Interest Accrual

A scheduled CRON job runs every day at **00:05 (Asia/Ho_Chi_Minh)**:

```java
@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Ho_Chi_Minh")
public void run()
```

This job:
1. Calculates daily interest for all **ACTIVE** subscriptions
2. Matures subscriptions that have reached their term date
3. Updates the liquidity pool accordingly

---

## 📁 Configuration Profiles

| Profile | Usage |
|---|---|
| `local` | Local development with Docker PostgreSQL |
| `prod` | Production on Render + Supabase |

Key config per profile:

```yaml
# local
jpa.hibernate.ddl-auto: none
flyway.baseline-on-migrate: false

# prod
jpa.hibernate.ddl-auto: validate
flyway.baseline-on-migrate: false
```

---

## 🌐 Deployment

- **Frontend**: [Vercel](https://vercel.com)
- **Backend**: [Render](https://render.com) (Web Service)
- **Database**: [Supabase](https://supabase.com) PostgreSQL — Session Pooler (port 5432)
- **Uptime monitoring**: [UptimeRobot](https://uptimerobot.com) — ping every 5 minutes to prevent Render free tier sleep

### Environment Variables (Render)

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | Supabase session pooler JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `PORT` | Server port (default: 8080) |

---

## 📊 Key Domain Concepts

**LockedProduct** — A savings product with a fixed term (days) and interest rate. Controls available quota.

**Subscription** — Acts as a **contract** between the user and a locked product. Records all lifecycle events:
- Daily interest accrual
- Early redemption
- Maturity

On each event, Subscription sends the result to **Earning** to update the balance.
States: `ACTIVE → MATURED / EARLY_REDEEMED`

**Earning** — Acts as a **sub-wallet** per subscription. This is where interest accumulates daily. To withdraw funds back to the main wallet, the user must go through Earning. Tracks:
- `available` — balance the user can withdraw
- `principal` — original invested amount
- `totalInterest` — accumulated interest to date
- `progress` — % of term completed

**Wallet** — The user's main wallet. Funds are locked into a Subscription on investment. To retrieve funds (interest + principal), the user withdraws from Earning back to Wallet.

**LiquidityPool** — Admin-managed pool that funds daily interest payouts and redemptions. Every payout is recorded in the **LiquidityLedger** for full audit trail.

---

## 🔒 Optimistic Locking

Critical entities use `@Version` to prevent race conditions:
- `WalletEntity`
- `EarningEntity`
- `LockedProductEntity`
- `LiquidityPoolEntity`
