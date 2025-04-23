# 📦 Swift API

A Spring Boot application providing a RESTful API for managing SWIFT codes. It supports operations such as adding, retrieving, and deleting SWIFT codes, and can also load initial data from an Excel file.

---

## 🚀 Features

- Load SWIFT codes from an Excel file on startup  
- Search SWIFT codes by code or country  
- Add and delete SWIFT codes via REST endpoints  
- Dockerized application with PostgreSQL support  
- In-memory H2 database for testing

---

## ✅ Prerequisites

- Java 17  
- Maven  
- Docker & Docker Compose

---

## 🛠️ Getting Started

### 1. Run the Application with Docker Compose

Start the application and PostgreSQL database using Docker Compose:

```bash
docker-compose up --build
```

This will:

- Build the Docker image for the application  
- Start the app on `http://localhost:8080`  
- Launch a PostgreSQL database on `localhost:5432`

---

### 3. API Endpoints

You can interact with the API using Postman, `curl`, or any REST client.

| Method   | Endpoint                                      | Description                                 |
|----------|-----------------------------------------------|---------------------------------------------|
| `GET`    | `/v1/swift-codes/{swiftCode}`                 | Retrieve a SWIFT code by code               |
| `GET`    | `/v1/swift-codes/country/{countryISO2}`       | Get SWIFT codes by country (ISO 2-letter)   |
| `POST`   | `/v1/swift-codes`                             | Add a new SWIFT code                        |
| `DELETE` | `/v1/swift-codes/{swiftCode}`                 | Delete a SWIFT code                         |

---

### 4. Stop the Application

To stop and remove the containers:

```bash
docker-compose down
```

---

## 🧪 Testing

Run unit and integration tests using:

```bash
mvn test
```

Tests are executed against an in-memory H2 database, configured in:

```
src/test/resources/application.properties
```

---

## 🧯 Troubleshooting
- **Port conflicts:** Make sure ports `8080` (app) and `5432` (PostgreSQL) are not already in use.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
