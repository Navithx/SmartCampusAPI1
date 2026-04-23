 
SmartCampus API

 Overview

The SmartCampus API is a RESTful web service developed to manage smart campus resources including rooms, sensors, and sensor readings. It supports full CRUD operations, validation, filtering, nested resources, and robust error handling.

 

  API Design

The API follows RESTful principles:

- Uses HTTP methods: GET, POST, DELETE
- Returns appropriate HTTP status codes (200, 201, 404, 409, 422, 500)
- JSON is used for request and response bodies
- Versioned base path:  
  `/api/v1`

  Main Resources

- `/api/v1` → Discovery endpoint  
- `/api/v1/rooms` → Room management  
- `/api/v1/sensors` → Sensor management  
- `/api/v1/sensors/{id}/readings` → Sensor readings  

 

 How to Build and Run the Project

 Prerequisites

- Java JDK 17+
- Maven
- Apache Tomcat (installed and running)

 
 Step-by-Step Instructions

1. Clone the repository

```bash
git clone https://github.com/Navithx/SmartCampusAPI1.git
cd SmartCampusAPI1
````

2. Build the project

```bash
mvn clean install
```

3. Deploy to Tomcat

* Copy the generated `.war` file from:

```
target/SmartCampusAPI1-1.0-SNAPSHOT.war
```

* Paste it into:

```
Tomcat/webapps/
```

4. Start Tomcat server

5. Open in browser:

```
http://localhost:8080/SmartCampusAPI1/api/v1/
```

 

  Sample cURL Commands

 1. Discovery Endpoint

```bash
curl -X GET http://localhost:8080/SmartCampusAPI1/api/v1/
```

 

  2. Create a Room

```bash
curl -X POST http://localhost:8080/SmartCampusAPI1/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Lab 1"}'
```

 

 3. Get All Rooms

```bash
curl -X GET http://localhost:8080/SmartCampusAPI1/api/v1/rooms
```

 

  4. Create a Sensor

```bash
curl -X POST http://localhost:8080/SmartCampusAPI1/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"TEMPERATURE","roomId":"R1"}'
```

 

  5. Add Sensor Reading

```bash
curl -X POST http://localhost:8080/SmartCampusAPI1/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"r1","timestamp":1710000000,"value":26.5}'
```

 

  6. Filter Sensors by Type

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI1/api/v1/sensors?type=TEMPERATURE"
```

 

 Error Handling

The API uses custom exception mappers to ensure no internal errors are exposed:

* 404 → Resource not found
* 409 → Room has active sensors
* 422 → Invalid linked resource
* 403 → Sensor unavailable
* 500 → Internal server error

 

 Logging

A logging filter is implemented using JAX-RS:

* Logs every incoming request (method + URI)
* Logs every outgoing response (status code)
* Improves debugging and observability

 
 Conceptual Answers

 Part 1: Service Architecture & Setup

In JAX-RS, resource classes are created per request by default. This means a new instance of the resource class is instantiated for every incoming HTTP request. This approach improves thread safety because no instance variables are shared across requests.

However, the application uses shared in-memory data structures such as HashMaps and ArrayLists stored in a central DataStore. Since multiple requests can access these shared structures simultaneously, there is a risk of race conditions and data inconsistency.

To manage this, data is centralized and controlled carefully. In a production system, thread-safe collections such as ConcurrentHashMap or synchronization mechanisms would be used to ensure safe concurrent access.

 

Hypermedia (HATEOAS) allows APIs to include links within responses that guide clients on what actions are available next. Instead of relying on static documentation, clients dynamically discover API capabilities.

This improves flexibility, reduces dependency on hardcoded endpoints, and allows the API to evolve without breaking clients. It also makes the system more self-descriptive and easier for developers to use.

 

 Part 2: Room Management

Returning only IDs reduces response size and improves network efficiency but requires additional API calls from the client to fetch full details.

Returning full objects increases response size but reduces the number of requests needed and simplifies client-side processing. In this implementation, full objects are returned to improve usability and reduce client complexity.

 

The DELETE operation is idempotent. When a room is deleted successfully, repeating the same DELETE request will not change the system further. The first request deletes the resource, and subsequent requests return a 404 since the resource no longer exists. The system state remains unchanged after the first deletion.

 

 Part 3: Sensor Operations & Linking

The @Consumes(MediaType.APPLICATION_JSON) annotation ensures that only JSON input is accepted. If a client sends data in another format such as text/plain or application/xml, JAX-RS will reject the request with HTTP 415 (Unsupported Media Type). This guarantees consistent data handling and proper validation.

 

Using @QueryParam for filtering is more flexible than using path parameters. Query parameters allow optional filtering, support multiple conditions, and are better suited for searching collections.

Path parameters are intended for identifying specific resources, while query parameters are ideal for filtering and querying datasets.

 

 Part 4: Deep Nesting with Sub-Resources

The Sub-Resource Locator pattern improves code organization by delegating nested resource logic to separate classes. Instead of handling everything in one large controller, responsibilities are divided.

This makes the code easier to maintain, improves readability, and allows the API to scale more effectively. It also promotes separation of concerns by isolating different functionalities into dedicated classes.

 

  Part 5: Advanced Error Handling

HTTP 422 is more appropriate than 404 when the request is valid but contains incorrect data. The server understands the request structure, but the referenced resource inside the payload does not exist. This makes 422 more semantically accurate than 404.

 
 
Exposing internal stack traces is a security risk because it reveals sensitive implementation details such as class names, file paths, frameworks, and method flows. Attackers can use this information to identify vulnerabilities and launch targeted attacks. Hiding stack traces prevents information leakage and strengthens security.

 

Using JAX-RS filters for logging centralizes logging logic and avoids code duplication. It ensures consistent logging across all endpoints and separates logging from business logic. This makes the application cleaner, easier to maintain, and more scalable compared to manually inserting logging statements in every method.

 
