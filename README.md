Overview

The SmartCampus API is a RESTful web service designed to manage smart campus resources such as rooms, sensors, and sensor readings.

Key Features

Manage Rooms
Manage Sensors assigned to rooms
Store and retrieve Sensor Readings
Proper error handling using exception mappers
Logging filter for request/response tracking

API Design

The API follows REST principles:

Uses standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`)
Returns appropriate HTTP status codes (200, 201, 404, 409, etc.)
JSON is used for request and response bodies
Organized into resources:

  * `/rooms`
  * `/sensors`
  * `/readings`


How to Build and Run the Project

Prerequisites

Make sure you have installed:

Java JDK 17+
Maven

Step-by-Step Instructions

1. Clone the repository

```bash
git clone https://github.com/Navithx/SmartCampusAPI1.git
cd SmartCampusAPI1
```

2. Build the project

```bash
mvn clean install
```

3. Run the application

```bash
java -jar target/SmartCampusAPI1-1.0-SNAPSHOT.jar
```

4. Server will start at:

```bash
http://localhost:8080/api
```

 Sample cURL Commands

1. Create a Room

```bash
curl -X POST http://localhost:8080/api/rooms \
-H "Content-Type: application/json" \
-d '{"name": "Lab A", "capacity": 30}'
```

2. Get All Rooms

```bash
curl -X GET http://localhost:8080/api/rooms
```

3. Create a Sensor

```bash
curl -X POST http://localhost:8080/api/sensors \
-H "Content-Type: application/json" \
-d '{"type": "Temperature", "roomId": 1}'
```

4. Add a Sensor Reading

```bash
curl -X POST http://localhost:8080/api/readings \
-H "Content-Type: application/json" \
-d '{"sensorId": 1, "value": 25.5}'
```

5. Get Sensor Readings

```bash
curl -X GET http://localhost:8080/api/readings?sensorId=1
```


Error Handling

The API includes:

Custom exception mappers (e.g., `NotFoundMapper`, `GenericExceptionMapper`)
Proper HTTP responses:

   `404 Not Found`
   `409 Conflict`
   `500 Internal Server Error`

Logging

A logging filter is implemented to:

 Log incoming requests
 Log outgoing responses
 Improve debugging and observability
