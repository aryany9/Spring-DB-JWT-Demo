

# 🛡️ Spring Boot JWT Authentication Demo

A clean and minimal implementation of **JWT-based authentication and authorization** using Spring Boot.

---

## 🔧 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- BCrypt for password hashing
- Maven
- H2 / PostgreSQL (you can configure your own DB)
- Postman (collection included)

---


## 📁 Project Structure Overview
```
├── src/main/java/com/example/demo
│   ├── config                 # Security configuration (filters, password encoder, etc.)
│   ├── controller             # REST controller for login/auth APIs
│   ├── models
│   │   ├── dto                # Entities: Customer, Credentials, Token, UserDetails
│   │   ├── request            # DTO for login request
│   │   └── response           # DTO for login response with tokens
│   ├── repositories           # JPA repositories for database access
│   ├── securities             # JWT utility for generating and validating tokens
│   ├── services               # Business logic: AuthService, UserDetailsService
│   └── type                   # Enum for user roles
│
├── resources
│   ├── application.properties # Configuration (DB, security, etc.)
│
├── postman_collection.json    # Pre-built Postman requests for testing
├── sample_query.sql           # Sample SQL to insert test users
```

---

## 🚀 How It Works

### ✅ 1. Authentication Flow

- **Login API**: `/api/auth/login`
  - Takes `username` and `password`.
  - If valid, returns an `accessToken` and `refreshToken`.
  - Access token is short-lived, refresh token is longer-lived.

- **JWT Filter**:
  - Intercepts incoming requests.
  - Extracts JWT from headers.
  - Validates it and sets user details in the security context.

- **Token Storage**:
  - Refresh tokens are stored in the `token` table.
  - Used to verify reuse and prevent token theft.

---

### 🔒 2. Security Config

Defined in `SecurityConfig.java`:
- All `/api/auth/**` endpoints are public.
- Other routes are protected and require valid JWT.
- Uses stateless session management.

---

### 🧍 3. Entities

- `Customer`: Basic customer info (email, name).
- `CustomerCredentials`: Contains login info (username, password, role).
- `Token`: Tracks issued refresh tokens.
- `CustomUserDetails`: Adapter to work with Spring Security.

---

### 🔄 4. Refresh Token

- API: `/api/auth/refresh-token`
- Pass refresh token to get a new access token.
- Refresh token must be valid and not expired/revoked.

---

## 🧪 Testing with Postman

- Open `postman_collection.json` in Postman.
- Includes:
  - Login
  - Refresh Token
  - Protected Routes (add Authorization header: `Bearer <token>`)

---

## 🧬 Sample SQL

Use `sample_query.sql` to insert test users:

```sql
-- Add customer
INSERT INTO customer (id, name, email)
VALUES (1, 'John Doe', 'john.doe@example.com');

-- Add credentials (hashed password is for "password123")
INSERT INTO customer_credentials (id, username, password, role, customer_id)
VALUES (
    1,
    'johnny',
    '$2a$10$Dow1j0JGjY7r9IPoEfU0xu3cgph4Z1RMejLRVdlD0Zprp/B5OaIgy',
    'ROLE_USER',
    1
);
```

---

## ⚙️ Run the Project

```bash
# Build and run
./mvnw spring-boot:run
```

Server runs on `http://localhost:8080`

---

## 💡 Future Improvements

- Add logout/revoke token support
- Role-based authorization (admin/user)
- Token expiration refresh mechanism
- Frontend integration

---

## 🙌 Author

Built by Aryan Yadav – for learning and demo purposes.  
Feel free to clone, fork, or suggest improvements!
