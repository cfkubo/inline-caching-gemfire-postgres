# Inline Caching with GemFire

This project demonstrates the implementation of inline caching using Apache Geode (GemFire) in a Spring Boot application. The goal is to improve application performance by caching frequently accessed data in memory, reducing the need for repeated database queries.

## Project Structure

```
inline-caching-gemfire
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── InlineCachingGemfireApplication.java
│   │   │           ├── config
│   │   │           │   └── GemFireConfig.java
│   │   │           ├── controller
│   │   │           │   └── DataController.java
│   │   │           ├── service
│   │   │           │   └── DataService.java
│   │   │           └── model
│   │   │               └── DataItem.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           └── index.html
├── pom.xml
└── README.md
```

## Features

- **Inline Caching**: Utilizes GemFire to cache frequently accessed data, improving performance and reducing database load.
- **Spring Boot Integration**: Fully integrated with Spring Boot for easy setup and configuration.
- **RESTful API**: Provides a simple RESTful API to interact with the cached data.

## Data Model

The application uses a simple table in PostgreSQL:

**Table: `data_item`**
| Column      | Type    | Description         |
|-------------|---------|---------------------|
| id          | BIGINT  | Primary key         |
| name        | TEXT    | Name of the item    |
| description | TEXT    | Description         |

The corresponding Java model is `com.example.model.DataItem`.

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL (with a `data_item` table and sample data)
- Gemfire

### Installation

### Postgres setup

```
CREATE TABLE data_item (
    id SERIAL PRIMARY KEY, -- 'id' will automatically generate unique, sequential integers
    name VARCHAR(255) NOT NULL,
    description TEXT
);


INSERT INTO data_item (id, name, description) VALUES (1, 'Widget', 'A useful widget');


INSERT INTO data_item (name, description)
SELECT
    'Product ' || SUBSTRING(MD5(RANDOM()::TEXT) FROM 1 FOR 3) || FLOOR(RANDOM() * 9000 + 1000)::TEXT,
    'This is a randomly generated description for item ' || gs.i || '. It highlights various attributes and features, such as ' ||
    CASE FLOOR(RANDOM() * 3)
        WHEN 0 THEN 'durability'
        WHEN 1 THEN 'efficiency'
        ELSE 'ease of use'
    END ||
    ', and is ideal for ' ||
    CASE FLOOR(RANDOM() * 4)
        WHEN 0 THEN 'home users.'
        WHEN 1 THEN 'professional environments.'
        WHEN 2 THEN 'outdoor adventures.'
        ELSE 'everyday tasks.'
    END ||
    ' Additional unique identifier: ' || MD5(RANDOM()::TEXT)
FROM generate_series(1, 1000) as gs(i);

```


1. Clone the repository:
   ```
   git clone https://github.com/yourusername/inline-caching-gemfire.git
   cd inline-caching-gemfire
   ```

2. Build the project using Maven:
   ```
   mvn clean install
   ```

3. Configure your database connection in `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
   spring.datasource.username=youruser
   spring.datasource.password=yourpassword
   ```

4. **Start the GemFire server and create the region:**

   - Start a GemFire locator and server:
     ```
     gfsh
     start locator --name=locator1
     start server --name=server1
     ```

   - Create the region for caching:
     ```
     create region --name=dataItemRegion --type=PARTITION
     ```

5. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

### Usage

- Access the application at `http://localhost:8080`.
- Use the REST endpoint to fetch data by ID:
  ```
  GET http://localhost:8080/data/{id}
  ```
  - On first request, data is loaded from PostgreSQL and cached in GemFire.
  - Subsequent requests for the same ID are served from the cache.

### Testing the Cache

1. **Insert sample data into PostgreSQL:**
   ```sql
   INSERT INTO data_item (id, name, description) VALUES (1, 'Widget', 'A useful widget');
   ```

2. **Test cache behavior:**
   - Make a GET request:
     ```
     curl http://localhost:8080/data/1
     ```
     - The first call fetches from the database and caches the result.
     - Subsequent calls fetch directly from GemFire (no database hit).


3. **To verify caching:**
   - Enable SQL logging in `application.properties`:
     ```
     spring.jpa.show-sql=true
     ```
   - Observe that only the first request triggers a SQL query.

4. **Example curl commands to test the API:**

   - Fetch data with ID 1 (first call, loads from DB and caches):
     ```
     curl -i http://localhost:9989/data/1
     ```

   - Fetch the same data again (should be served from cache, no DB hit):
     ```
     curl -i http://localhost:9989/data/1
     ```

   - Fetch data with a different ID (replace `2` with your actual data):
     ```
     curl -i http://localhost:9989/data/2
     ```

   - You can observe the difference in SQL logs between the first and subsequent requests.

...

### GemFire Setup Notes

- Ensure GemFire server is running and the `dataItemRegion` region is created before starting the Spring Boot app.
- The region type should be `PARTITION` or `REPLICATE` as needed.
- The application connects as a GemFire client and uses the region for caching.

## Conclusion

This project serves as a practical example of how to implement inline caching with GemFire in a Spring Boot application. By caching frequently accessed data, you can significantly enhance the performance and scalability of your applications. Experiment with different data and observe the reduction in database queries thanks to GemFire's inline cache!# inline-caching-gemfire-postgres
