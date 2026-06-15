# API Contract Conventions

- Prefix: `/api/v1`
- Correlation: clients may send `X-Correlation-ID`; the platform always returns it.
- Paging: `page`, `size`, and repeatable `sort=field,direction` query parameters.
- Filtering: domain endpoints accept an `owner` filter in the runnable reference implementation.
- Content: JSON by default; selected collection endpoints support `Accept: application/xml`.
- Validation: invalid commands return field-level errors with HTTP `400`.
- Authentication: bearer JWT issued by Auth Service; roles are included in `roles`.
- Errors: timestamp, status, message, path, correlation ID, and validation fields.

## Event contracts

Every event contains:

```json
{
  "eventId": "uuid",
  "eventType": "TRANSFER_REQUESTED",
  "occurredAt": "2026-06-15T14:30:00Z",
  "correlationId": "CG-...",
  "payload": {}
}
```

Core topics: `transaction-created`, `transfer-requested`, `fraud-alert-created`, and `notification-created`.
