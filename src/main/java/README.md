# Smart Campus REST API

## Overview

This project is a RESTful API developed using Java and JAX-RS (Jersey).
It simulates a Smart Campus system that manages Rooms, Sensors, and Sensor Readings.

---

## Technologies Used

* Java 17
* JAX-RS (Jersey)
* Maven
* Grizzly HTTP Server
* JSON (Jackson)
* Postman

---

## How to Run

```bash
mvn clean compile
mvn exec:java
```

Open:
http://localhost:8080/api/v1

---

## API Endpoints

### Rooms

* GET /rooms
* POST /rooms
* GET /rooms/{id}
* DELETE /rooms/{id}

### Sensors

* GET /sensors
* GET /sensors?type=Temperature
* POST /sensors

### Readings

* GET /sensors/{id}/readings
* POST /sensors/{id}/readings

---

## Sample CURL

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"SCI-201","name":"Science Lab","capacity":40}'
```

---

# 📘 REPORT ANSWERS

## Part 1

**Q1: JAX-RS Resource Lifecycle**

JAX-RS resources are created per request by default. This means each HTTP request gets a new instance of the resource class. This avoids shared state issues and helps prevent race conditions when using in-memory data structures like HashMap.

---

**Q2: Hypermedia (HATEOAS)**

Hypermedia allows clients to navigate the API dynamically using links in responses. This improves usability because clients do not need hardcoded URLs and can discover available actions from the API response itself.

---

## Part 2

**Q1: Returning IDs vs Full Objects**

Returning only IDs reduces network usage but requires additional requests from the client. Returning full objects increases payload size but provides complete information in one response, improving client performance.

---

**Q2: DELETE Idempotency**

DELETE is idempotent because repeating the same request produces the same result. If the room is already deleted, the system will return a consistent response (e.g., not found), without changing the system state further.

---

## Part 3

**Q1: @Consumes JSON**

If a client sends data in a format other than JSON, JAX-RS cannot map the request body to Java objects. This results in a 415 Unsupported Media Type or 400 Bad Request error.

---

**Q2: QueryParam vs Path**

Using query parameters is better for filtering because it keeps the resource structure clean and allows flexible queries. Path parameters are better suited for identifying specific resources.

---

## Part 4

**Q1: Sub-Resource Locator**

Sub-resource locators help break down complex APIs into smaller components. This improves code organization and makes the system easier to maintain compared to handling everything in one class.

---

**Q2: Updating Sensor Value**

When a new reading is added, the sensor’s current value is updated. This ensures that the latest reading is always available without needing to query historical data.

---

## Part 5

**Q1: 409 Conflict**

This occurs when trying to delete a room that still contains sensors. The system prevents data inconsistency by blocking the operation.

---

**Q2: 422 Unprocessable Entity**

This is used when a sensor is created with a non-existing roomId. The request is valid but contains incorrect data.

---

**Q3: 403 Forbidden**

If a sensor is in MAINTENANCE mode, it cannot accept readings. The system blocks the request with a 403 error.

---

**Q4: 500 Internal Server Error**

Exposing stack traces is dangerous because attackers can learn about system structure and vulnerabilities. Therefore, a generic error message is returned.

---

**Q5: Logging Filters**

Using filters is better than adding logging manually in each method because it centralizes the logic and reduces code duplication.

---

## Logging

* Logs all incoming requests
* Logs all outgoing responses

---

## Video Demonstration

The video demonstrates:

* API functionality
* Postman testing
* Error handling

---

## Author

Name: A.W.C.Pemathilaka
Module: 5COSC022W
