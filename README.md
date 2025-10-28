# Fibonacci REST Service

A high-performance RESTful web service for calculating Fibonacci sequences using multi-threaded computation. Built with Spring Boot, this service provides fast and efficient Fibonacci number generation through parallel processing.

## Features

- **RESTful API** - Simple HTTP endpoints for Fibonacci calculations
- **Multi-threaded Processing** - Utilizes parallel computation for efficient calculation
- **Spring Boot** - Modern Spring Boot application with auto-configuration
- **GZIP Compression** - Automatic response compression for optimized bandwidth
- **Aspect-Oriented Logging** - Comprehensive logging with AOP
- **Security** - Built-in Spring Security integration
- **Profile Support** - Separate configurations for development and production environments
- **Cloud Ready** - Spring Cloud integration for Cloud Foundry deployments
- **CI/CD** - Azure Pipelines configuration included

## Technology Stack

- **Java 1.7**
- **Spring Boot 1.2.0.RC1**
- **Spring Framework** - Core, Web, Security, AOP
- **Spring Cloud 1.1.0.RELEASE** - Cloud Foundry connector
- **Maven 3.0+** - Build and dependency management
- **Embedded Tomcat** - Application server
- **Logback** - Logging framework
- **Jackson** - JSON processing

## Prerequisites

- JDK 1.7 or higher
- Maven 3.0 or higher

## Installation & Configuration

### 1. Clone the Repository

```bash
git clone <repository-url>
cd fibonacci-rest-service
```

### 2. Configuration Files

The application uses profile-based configuration:

- **application.properties** - Base configuration for JVM parameters and execution timeouts
- **application.yml** - Security and management settings
- **application-dev.yml** - Development environment configuration
- **application-prod.yml** - Production environment configuration

### Key Configuration Properties

Edit `src/main/resources/application.properties` to customize:

```properties
# JVM parameters
jvm.parameters.maxheapsize=-Xmx512m

# Default timeout (15 minutes) in milliseconds
task.execution.timeout.default=900000

# Progress report interval
task.execution.progress.report.interval=10000
```

## Running the Application

### Development Mode (Default)

```bash
# Using Maven wrapper (Unix/Mac)
./mvnw spring-boot:run

# Using Maven wrapper (Windows)
mvnw.cmd spring-boot:run

# Using installed Maven
mvn spring-boot:run
```

The application will start with the `dev` profile by default and enable remote debugging on port 5005.

### Production Mode

```bash
mvn spring-boot:run -Pprod
```

Or with command-line arguments:

```bash
java -jar target/fibonacci-0.0.1-SNAPSHOT.war --spring.profiles.active=prod
```

### Building for Deployment

```bash
# Development build
mvn clean package

# Production build
mvn clean package -Pprod
```

The WAR file will be generated in the `target/` directory.

## API Endpoints

### Calculate Fibonacci Sequence

**Endpoint:** `GET /v1/rest/fibonacci/{n}`

**Description:** Calculate and return the first N Fibonacci numbers.

**Parameters:**
- `n` (path parameter) - The count of Fibonacci numbers to generate (must be a positive integer)

**Success Response:**
- **Code:** 200 OK
- **Content:** Space-separated Fibonacci numbers
- **Example:** `0 1 1 2 3 5 8 13 21 34`

**Error Responses:**

- **Code:** 400 BAD REQUEST
  - **Content:** `Invalid number - {n}`
  - **Condition:** When n is not a valid positive integer

- **Code:** 408 REQUEST TIMEOUT
  - **Content:** `Please contact administrator.`
  - **Condition:** When calculation exceeds timeout threshold

- **Code:** 500 INTERNAL SERVER ERROR
  - **Content:** `Please contact administrator.`
  - **Condition:** When an unexpected error occurs

### Example Requests

```bash
# Calculate first 10 Fibonacci numbers
curl http://localhost:8080/v1/rest/fibonacci/10

# Response: 0 1 1 2 3 5 8 13 21 34

# Calculate first 20 Fibonacci numbers
curl http://localhost:8080/v1/rest/fibonacci/20
```

## Project Structure

```
src/
├── main/
│   ├── java/com/emc/test/
│   │   ├── FibonacciRestServiceApplication.java    # Main application class
│   │   ├── ServletInitializer.java                 # WAR deployment initializer
│   │   ├── common/                                 # Common utilities and exceptions
│   │   │   ├── BaseException.java
│   │   │   ├── SystemException.java
│   │   │   ├── Consts.java                        # Application constants
│   │   │   └── utils/                             # Utility classes
│   │   │       ├── ConfigurationUtils.java
│   │   │       ├── JSONReadWriteHelper.java
│   │   │       └── NioFileSystemUtils.java
│   │   ├── config/                                # Spring configuration
│   │   │   └── WebConfigurer.java
│   │   ├── fibonacci/                             # Fibonacci calculation logic
│   │   │   ├── AtomicFloat.java
│   │   │   ├── FibonacciCoreCalucaltion.java
│   │   │   └── FibonacciPartThread.java          # Multi-threaded processor
│   │   ├── filter/                                # Servlet filters
│   │   │   ├── GZipServletFilter.java            # GZIP compression
│   │   │   ├── GZipResponseUtil.java
│   │   │   ├── GZipServletOutputStream.java
│   │   │   └── GZipServletResponseWrapper.java
│   │   ├── logging/                               # AOP logging
│   │   │   └── LoggingAspect.java
│   │   ├── process/                               # Process execution
│   │   │   └── ProcessRunner.java
│   │   └── rest/                                  # REST controllers
│   │       └── FibonacciCalculationResource.java
│   └── resources/
│       ├── application.properties
│       └── config/
│           ├── application.yml
│           ├── application-dev.yml
│           └── application-prod.yml
└── test/
    └── java/com/emc/test/
        └── FibonacciRestServiceApplicationTests.java
```

## Architecture

### Multi-threaded Calculation

The application uses a sophisticated multi-threading approach:

1. **Request Processing** - REST endpoint receives the request
2. **Thread Pool Creation** - Creates a pool of threads based on the calculation size
3. **Parallel Computation** - Each thread calculates a portion of the sequence
4. **Result Aggregation** - Results are written to temporary files and aggregated
5. **Cleanup** - Temporary files are removed after response is sent

### Thread Distribution

- Calculations are divided into chunks based on `MAX_THREADHOLD` constant
- Each thread processes a portion of the sequence independently
- Results are synchronized using atomic operations and file I/O

## Profiles

### Development Profile (`dev`)

- Debug logging level
- Remote debugging enabled (port 5005)
- Detailed error messages

### Production Profile (`prod`)

- Info logging level
- Optimized for performance
- Reduced verbosity

## Testing

Run the test suite:

```bash
mvn test
```

Run tests with coverage:

```bash
mvn clean verify
```

## Building & Deployment

### Maven Commands

```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build WAR file
mvn clean package

# Run with specific profile
mvn spring-boot:run -Dspring.profiles.active=prod
```

### Deployment Options

1. **Standalone WAR** - Deploy to any servlet container (Tomcat, Jetty, etc.)
2. **Embedded Server** - Run as executable WAR with embedded Tomcat
3. **Cloud Foundry** - Push to Cloud Foundry platform
4. **Azure** - Use included Azure Pipelines configuration

## Performance Considerations

- **JVM Heap Size**: Default is 512MB, adjust `jvm.parameters.maxheapsize` as needed
- **Execution Timeout**: Default is 15 minutes, configurable via `task.execution.timeout.default`
- **Thread Pool**: Automatically sized based on calculation requirements
- **GZIP Compression**: Enabled by default for bandwidth optimization

## Logging

The application uses Logback for logging with AOP-based method-level logging:

- **Debug Mode**: Detailed execution logs including method entry/exit
- **Production Mode**: Error and info level logs only
- **Performance Tracking**: Execution time tracking for calculations

## Security

- Spring Security is integrated but basic auth is disabled by default
- Management endpoints are accessible without authentication
- Configure security settings in `application.yml`

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Default port is 8080, change via `server.port` property

2. **Out of Memory Errors**
   - Increase heap size in `application.properties`
   - Reduce calculation size or increase timeout

3. **Timeout Errors**
   - Increase `task.execution.timeout.default` for larger calculations
   - Consider optimizing thread pool size

## License

Copyright © EMC Corporation

## Author

Jason.Wang

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
