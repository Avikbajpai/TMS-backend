# NexusTMS - Spring Boot Backend

This is the Java Spring Boot implementation of the NexusTMS API.

## 🛠️ Requirements
- Java 17 or higher
- Maven 3.8+

## 🚀 How to Run
1. Navigate to this directory:
   ```bash
   cd backend-java
   ```
2. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on [http://localhost:8080](http://localhost:8080).

## 📊 Features
- **H2 In-Memory Database**: Ready to use out of the box.
- **RESTful API**: Matching the React frontend expectations.
- **Swagger/H2 Console**: Accessible at `/h2-console` (JDBC URL: `jdbc:h2:mem:nexustms`).
- **Lombok**: Used for clean models and DTOs.
- **Validation**: JSR-303 Bean Validation included.

## 🔗 Integration with Frontend
To connect the React frontend to this Java backend, update the `API_BASE` in `src/App.tsx` (frontend) to:
`const API_BASE = 'http://localhost:8080/api';`
