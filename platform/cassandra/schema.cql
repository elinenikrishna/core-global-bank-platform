CREATE KEYSPACE IF NOT EXISTS core_global_events
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS core_global_events.transaction_events_by_account (
  account_id text,
  event_day date,
  occurred_at timestamp,
  event_id uuid,
  event_type text,
  amount decimal,
  payload text,
  correlation_id text,
  PRIMARY KEY ((account_id, event_day), occurred_at, event_id)
) WITH CLUSTERING ORDER BY (occurred_at DESC);

CREATE TABLE IF NOT EXISTS core_global_events.platform_events_by_type (
  event_type text,
  event_day date,
  occurred_at timestamp,
  event_id uuid,
  payload text,
  PRIMARY KEY ((event_type, event_day), occurred_at, event_id)
) WITH CLUSTERING ORDER BY (occurred_at DESC);

