# Nestly Backend

Spring Boot and Kotlin backend for the Nestly Flutter app. It exposes the app's current mock workflows as REST APIs and persists data in MySQL.

## Stack

- Kotlin 2.2.20
- Spring Boot 3.5.14
- Spring Web, Validation, Data JPA, Actuator
- MySQL 8.4
- Flyway migrations
- Docker Compose

## Run with Docker

```bash
docker compose up --build
```

The API starts on `http://localhost:8080`. MySQL is exposed on `localhost:3306` with database/user/password `nestly`/`nestly`/`nestly`.

## Run Locally

Start MySQL first:

```bash
docker compose up mysql
```

Then run the app:

```bash
./gradlew bootRun
```

Override connection settings with environment variables when needed:

```bash
DB_URL='jdbc:mysql://localhost:3306/nestly?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
DB_USER=nestly \
DB_PASSWORD=nestly \
./gradlew bootRun
```

## API

### Health

```http
GET /api/health
GET /actuator/health
```

### Auth

```http
POST /api/auth/email
Content-Type: application/json

{
  "email": "buyer@example.com",
  "password": "secret"
}
```

```http
POST /api/auth/google
Content-Type: application/json

{
  "email": "google.user@nestly.app",
  "name": "Google User"
}
```

### Listings

```http
GET /api/listings
GET /api/listings?q=bike&category=Sports&condition=Like%20new&sort=popular&viewerEmail=sara@example.com
GET /api/listings/listing-bike?viewerEmail=sara@example.com
```

```http
POST /api/listings
Content-Type: application/json

{
  "title": "Vintage oak desk",
  "category": "Furniture",
  "price": 180,
  "condition": "Good",
  "location": "Prenzlauer Berg",
  "description": "Solid oak writing desk with three drawers and brass pulls.",
  "sellerName": "Maya",
  "sellerEmail": "maya@nestly.app"
}
```

If `photo` is omitted, the backend assigns the same default category artwork used by the Flutter mock repository.

### Photos

```http
GET /api/photos/default?category=Furniture
```

### Conversations

```http
GET /api/conversations
GET /api/conversations?buyerEmail=buyer@example.com
GET /api/conversations/{id}
```

```http
POST /api/conversations
Content-Type: application/json

{
  "listingId": "listing-bike",
  "buyerEmail": "buyer@example.com",
  "buyerName": "Buyer"
}
```

```http
POST /api/conversations/{id}/messages
Content-Type: application/json

{
  "authorEmail": "buyer@example.com",
  "authorName": "Buyer",
  "body": "Is this still available?"
}
```

## Flutter Integration Notes

Replace the current in-memory repositories with HTTP clients that call:

- `AuthRepository` -> `/api/auth/email` and `/api/auth/google`
- `ListingsRepository` -> `/api/listings`
- `PhotoRepository` -> `/api/photos/default`
- `ChatRepository` -> `/api/conversations` and `/api/conversations/{id}/messages`

For Android emulator builds, use `http://10.0.2.2:8080` instead of `http://localhost:8080`.
