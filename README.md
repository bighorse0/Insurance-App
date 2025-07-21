# Insurance-App

## Features

- **CRUD for Clients, Claims, and Insurance Policies**
- **Search & Filter Endpoints:**
  - Get all claims for a given policy: `GET /api/claimsByPolicy/{policyId}`
  - Get all claims by status: `GET /api/claimsByStatus/{claimStatus}`
  - Get all clients for a given policy: `GET /api/clientsByPolicy/{policyId}`
- **Summary Endpoint:**
  - Get number of claims per client: `GET /api/claimCountPerClient`
- **Validation and error handling**
- **In-memory H2 database for easy testing and development**

## Advanced Features

### Advanced Filtering (Claims)
- Filter claims by date range, amount range, and status:
  - `GET /api/claims/filter?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD&minAmount=100&maxAmount=200&status=OPEN`
  - Any combination of filters is supported.

### Soft Delete (Claims)
- Claims are never physically deleted; instead, they are marked as deleted (`isDeleted=true`).
- Soft-deleted claims are excluded from all queries and filters by default.
- Restore a soft-deleted claim:
  - `POST /api/restoreClaim/{claimId}`

### Audit Logging (Claims)
- All create, update, delete (soft), and restore actions for claims are logged in the `ClaimAuditLog` table.
- Each log entry records: claimId, action (CREATE, UPDATE, DELETE, RESTORE), timestamp, claim details, and username (future use).

## Running & Testing

- The app is configured to use an in-memory H2 database by default (no MySQL required).
- To run tests:
  1. Ensure you are using Java 17+ and Maven is set to use Java 17.
  2. Run: `mvn clean test`
- To run the app:
  1. Start with: `mvn spring-boot:run`
  2. Access the H2 console at `/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, password: empty)

## Endpoints Overview

- `POST   /api/createClient/{policyId}`
- `GET    /api/getClient/{clientId}`
- `PUT    /api/updateClient/{clientId}`
- `DELETE /api/deleteClient/{clientId}`
- `GET    /api/displayAllClients`
- `GET    /api/clientsByPolicy/{policyId}`
- `GET    /api/claimCountPerClient`

- `POST   /api/createClaim/{policyId}`
- `GET    /api/getClaim/{claimId}`
- `PUT    /api/updateClaim/{claimId}`
- `DELETE /api/deleteClaim/{claimId}`
- `GET    /api/displayAllClaims`
- `GET    /api/claimsByPolicy/{policyId}`
- `GET    /api/claimsByStatus/{claimStatus}`

- `POST   /api/saveInsurancePolicy`
- `GET    /api/getInsurancePolicy/{insurancePolicyId}`
- `DELETE /api/deleteInsurancePolicy/{insurancePolicyId}`
- `GET    /api/displayAllPolicy`

## Testing
- All features are covered by integration tests.
- To run tests: `mvn clean test` (ensure Java 17+ and H2 in-memory DB are used).

## Notes
- For production, switch back to MySQL by uncommenting the MySQL settings in `src/main/resources/application.properties` and providing your DB credentials.
- The app uses Spring Boot 3 and Java 17.
