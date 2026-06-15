# Architecture Decisions

## Service boundaries

Core Global uses business-capability boundaries rather than technical layers as deployable units. Each service owns its write model and publishes facts after accepting a command. Synchronous REST handles immediate client intent; Kafka handles downstream reactions.

## Consistency model

- Transfers and account commands execute inside a local database transaction.
- Cross-service behavior is eventually consistent through named Kafka events.
- Consumers must use the event ID as an idempotency key.
- Correlation IDs provide trace continuity across REST and event boundaries.

## Data placement

- Oracle-compatible relational tables hold customers, products, balances, and command state.
- Cassandra stores high-volume immutable event timelines partitioned by account and day.
- Redis is available for short-lived session and cache workloads.

## Cloud mapping

| Capability | AWS | Azure |
|---|---|---|
| Containers | ECS / EKS | App Service / AKS |
| Kafka | MSK | Event Hubs Kafka endpoint |
| SQL | RDS Oracle/PostgreSQL | Azure Database / Oracle on VM |
| Cassandra | Keyspaces | Cosmos DB Cassandra API |
| Secrets | Secrets Manager | Key Vault |
| Metrics | CloudWatch | Azure Monitor |

