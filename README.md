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


 Conceptual Answers

PART 1: Service Architecture and Setup 
 
1.    Project & Application Configuration

In JAX-RS a resource class is created fresh for each HTTP request. This means a new instance of the class is made every time a request comes in.
This design helps with two things:
·      It makes sure the class is thread-safe by default.
·      It ensures that there is no shared data that can be changed inside the class.
However, in this coursework we use in-memory data structures like HashMap and ArrayList to store data. These structures are usually defined in one shared class, like DataStore. Act like a single instance used throughout the application.
Since many requests can access these shared structures at the time:
·      There is a risk of things getting messed up because of timing issues.
·      The data might become inconsistent.
To handle this:
·      We put all the data in one place.
·      We are careful to avoid making changes to the data in a way.
·      In a production system we would need to use special tools, like synchronization or special collections that are safe for many threads to use at the same time like ConcurrentHashMap.
 
2.     Discovery Endpoint

Hypermedia, which is also known as HATEOAS or Hypermedia As The Engine Of Application State is a way for APIs to include links in the information they send back to clients. These links tell the clients what they can do next.
This means that clients do not have to follow a set of rules that never change. They can look at the links in the information they get from the API. Figure out what to do.
The good things about Hypermedia are:
·      It reduces the need for documentation that the client has to look at to know what to do.
·      It makes it easier for clients to find out what the API can do.
·      It makes the API more flexible and able to change over time.
·      It lets clients change automatically when the API changes.
 


Part 2: Room Management
 
1.    Room Resource Implementation 
 
Returning IDs or full objects has pros and cons:
 
·      Returning IDs:
o   The response size is smaller.
o   It uses network bandwidth.
o   The client needs to make extra requests.
 
·      Returning objects:
o   There is data, in one response.
o   It reduces the number of API calls.
o   It uses a bit bandwidth.
 
In this implementation we return objects. This is because it makes it easier for the client to process the data and reduces the number of requests they need to make.

      
2.    Room Deletion and Safety Logic
 
In this implementation, the DELETE operation is idempotent.
 
This means: 
·      If you delete a room successfully once, any further DELETE requests for that room will not change the system state any more.
What to do:
·      First, DELETE → the room is deleted → returns success.
·      Second DELETE: The room is no longer exists, and a 404 error is returned.  
Even though the system gives you messages the important thing is that the system does not change after the first time you delete the room, which is what we want to happen.
Also, deletion is blocked if the room has sensors, which keeps the data safe.
 
 
Part 3: Sensor Operations & Linking
 
1.    Sensor Resource & Integrity
 
The @Consumes(MediaType.The APPLICATION_JSON annotation makes sure that the API only accepts JSON data.
 
If a customer sends:
·      text/plain 
·      application/xml 
 
JAX-RS will reject the request. It will return HTTP 415 which means Unsupported Media Type.
This ensures that the data format is consistent. It also ensures that the data is parsed and validated properly.
 
2.    Filtered Retrieval & Search
 
Using @QueryParam to filter:
·      /api/v1/sensors?type=CO2
Is better than:
·      /api/v1/sensors/type/CO2
Because: 
·      Query parameters help you filter and search.
·      They can be changed and are not required.
·      It's easy to combine more than one filter.
For example:
·      /api/v1/sensors?type=CO2&status=ACTIVE
Path parameters are better for finding specific resources than for filtering collections.
 
 
 
Part 4: Deep Nesting with Sub-Resources
 
1.    Sub-Resource Locator Pattern
 
The Sub-Resource Locator pattern lets you give different classes the job of handling nested resource logic.
 
Advantages:
·      Makes code easier to find
·      Lessens the complexity of main resource classes
·      Makes it easier to read and maintain
·      Helps large APIs grow without problems
 
Instead of one big class doing everything, the work is divided up:
·      SensorResource → handles with sensors
·      SensorReadingResource → handles the readings.
 
 
 

Part 5: Advanced Error Handling, Exception Mapping & Logging
 
1.    Dependency Validation (422 Unprocessable Entity)

HTTP 422 is more semantically correct than 404 because the request itself is syntactically correct, but the data in the payload is not valid.  In this case:
·      The JSON structure is correct.
·      The endpoint is there
·      But there is no resource that is mentioned (like roomId)
A 404 means that the requested endpoint or resource can't be found,
while a 422 means that:
·      The server gets the request
·      But it can't process it because the data is wrong or doesn't match.
So, 422 is a better way to show that a valid request failed validation than that an endpoint is missing. 

 
2.    Global Safety Net (500)
 
Exposing internal Java stack traces is a security risk because it shows
private information about how the system works.
 
An attacker can collect data such as:
·      Names of classes and packages inside
·      Paths to files and directory structures
·      Frameworks and libraries that are being used
·      Names of methods and the flow of application logic
 
This information can be used to do things like:
·      Find known weaknesses in certain libraries
·      Do targeted attacks, like injection attacks.
·      Understand how the system is built so they can try to exploit it
 
The system lowers the chance of information leaks and makes the whole system safer by hiding stack traces and giving generic error messages.
 
 
 
3.    API Request & Response Logging Filters
 
Using JAX-RS filters for logging is helpful because it keeps logging logic in one place and stops resource methods from repeating themselves.
 
Some of the benefits are:
·      Separation of concerns means that logging is done separately from
·      business logic.
·      Less code duplication: You don't have to add logging statements to every
·      method.
·      Consistency: All requests and responses are logged in the same way.
·      Easier to keep up with: You can change how logging works in one place.
 
Adding Logger.info() by hand to each resource method, on the other hand,
makes the code more repetitive, harder to maintain, and more likely to log
inconsistently.

  My long-term goal is to become a Cyber Security Engineer, focusing on protecting systems, networks, and data from cyber threats. With the rapid growth of digital technology, I am motivated to contribute to securing digital infrastructures.

This course will give me a strong foundation in computing and essential cybersecurity skills such as network security, system architecture, and risk management. It will also help me develop practical experience in identifying vulnerabilities and implementing security solutions.

By completing this program, I aim to gain the technical expertise and problem-solving skills needed to succeed in the cybersecurity field and contribute effectively to the industry.
