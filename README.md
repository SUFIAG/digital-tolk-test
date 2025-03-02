Translation Management API

The Translation Management API is a RESTful service that allows users to store, retrieve, search, and manage translations for multiple locales. It supports tagging translations for contextual usage and ensures security and performance optimizations.

Features

Create, Update, and Delete Translations with locale and tags

Search Translations using locale, key, content, and tags

Export Translations for frontend applications (e.g., Vue.js)

Role-Based Access Control (RBAC) for secured API access

Security Best Practices including input validation, caching, and authorization

Setup Instructions

Prerequisites

Java 17

Maven

Postman (for API testing, optional)

Step 1: Clone the Repository

Step 2: Configure the Database

Start a H2 Database

Alternatively, update the application.yml file with database credentials.

Step 3: Build and Run the Application

 mvn clean install
 mvn spring-boot:run

The API will be available at: http://localhost:8086/api/translations

Step 4: Run Tests

 mvn test

API Endpoints

1️⃣ Create a Translation

POST /api/translations

{
  "key": "hello",
  "locale": "en",
  "content": "Hello",
  "tags": ["web", "mobile"]
}


![test_image_1](https://github.com/user-attachments/assets/1b74c882-db7f-4972-ab5e-82bef6aeb720)

![test_image_2](https://github.com/user-attachments/assets/f0756f57-a28b-4576-b429-1df3eaca8046)

![test_image_2 1](https://github.com/user-attachments/assets/d2cf37a8-4842-4fca-9705-24d56f298616)


2️⃣ Update a Translation

PUT /api/translations/{id}

{
  "key": "greeting",
  "locale": "en",
  "content": "Hi",
  "tags": ["desktop"]
}

![test_image_3](https://github.com/user-attachments/assets/a6a3f4ca-b48f-4e8e-9109-107006a27993)

![test_image_4](https://github.com/user-attachments/assets/eab767a7-7591-41cf-b3e9-b419d6491ecc)


3️⃣ Search Translations

GET /api/translations?locale=en&key=hello&tag=web&page=0&size=10

![test_image_5](https://github.com/user-attachments/assets/e3aa3583-6f14-4547-8618-3922501019bb)

4️⃣ Delete a Translation

DELETE /api/translations/{id}

![test_image_7](https://github.com/user-attachments/assets/0cc3fa7c-be40-4932-af3d-fee085cc36e8)

![test_image_8](https://github.com/user-attachments/assets/d72af1f1-8d18-4d31-bccd-47765bbdd50a)

5️⃣ Export Translations

GET /api/translations/export

![test_image_6](https://github.com/user-attachments/assets/52a6a412-65aa-477f-a9a7-c574077333ff)


Design Choices

1. Controller Layer

Defines REST endpoints for handling translation requests.

Uses ResponseEntity<> for proper HTTP status codes.

2. Service Layer

Implements business logic for translation storage and retrieval.

Ensures input validation and exception handling.

3. Repository Layer

Uses Spring Data JPA for interacting with the database.

Optimized queries for searching translations efficiently.

4. Security Measures

JWT Authentication (to be added for production use).

RBAC Authorization via @PreAuthorize.

Input Validation with @Valid.

Rate Limiting (recommended for production use).

Future Enhancements

Add pagination support for export endpoint.

Implement bulk import/export of translations.

Integrate Redis caching for improved response times.
