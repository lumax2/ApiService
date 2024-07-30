# API Service Project

## Module Description

Overseas API Service ApiDocs:
[API Documentation](https://streamnz.com/oversea/api/documents/)

## Features

- **Real-time Diagnostics**: Uses Arthas for real-time diagnostics and troubleshooting without modifying the code.
- **Performance Monitoring**: Implements SkyWalking for comprehensive application performance monitoring and management.
- **Dynamic Routing**: Utilizes Spring Cloud Zuul for dynamic routing, monitoring, resiliency, and security.
- **Externalized Configuration**: Uses Spring Cloud Config to manage external configurations across all environments.
- **Service Discovery**: Employs Spring Cloud Consul for service discovery and configuration management.
- **Entity Management**: Leverages Lombok to reduce boilerplate code for entity objects.
- **User Permissions**: Manages user permissions using Redis and SA-TOKEN.
- **Database**: Utilizes MySQL for relational database management.
- **Inter-service Communication**: Uses Spring Cloud OpenFeign for declarative REST client.

## Version Information

- **JDK Version**: 17
- **Spring Cloud Version**: 2021.0.x
- **Spring Boot Version**: 2.6.x

## How to Start

### Local Start
Use IDEA or Eclipse to start directly with the following JVM parameters:
--spring.profiles.active=dev
## API Documentation

- **Swagger**: [Swagger UI](http://localhost:port/swagger-ui/index.html)
- **Knife4j**: [Knife4j UI](http://localhost:port/doc.html)

### References:
- **Lombok**:
  - [Installation](https://blog.csdn.net/zhglance/article/details/54931430)
  - [Features](https://projectlombok.org/features/all)
- **Swagger2**:
  - [Swagger2 Guide](https://www.cnblogs.com/magicalSam/p/7197533.html)
- **Alibaba Code IDEA Plugin**:
  - Search for "Alibaba Code Guideline" in IDEA plugins and install
  - [GitHub Repository](https://github.com/alibaba/p3c)

## Development Conventions

### Exception Handling
- Only handle exceptions that need to be dealt with by the business logic.
- Avoid using try-catch blocks without any actions; ensure `e.printStackTrace` or `log.error` is used to record the exceptions.

### Transaction Handling
- When using `@Transactional`, avoid catching exceptions within the method as it prevents rollback.
- By default, only runtime exceptions trigger rollback. To rollback on specific exceptions or all exceptions, use `rollbackFor`.
  Example: `rollbackFor = Exception.class`

### Error Messages
- All error messages must be configured in `messages.properties`. Do not include any Chinese messages directly in the code.

### Constant Definitions
- All constants must be defined in `SystemConstants`.

### Avoid Mapping Unnecessary Fields
- If there is no specific operation needed, avoid mapping timestamp fields to the entity and use the database's `SYSDATE` instead.

### Use Rich Model
- Add business validations and logic in entity objects to implement a rich model.
- Utilize collection streams to filter data in the code layer to reduce database interactions.

## DON'T DO THIS
- Avoid using reflection in the code unless absolutely necessary.
- Avoid using threads in the code unless absolutely necessary.
- Do not use magic numbers in the business logic, such as `if (i == 1)`.
- For file attachments, since the system uses an open-source S3 implementation (Minio), do not store files locally or on server disks.

## Table Name Conventions

### Table Name Prefix
- `TM`  Master Data Table
- `TT`  Transaction Data Table
- `TR`  Relationship Table
- `TA`  Aggregation Table
- `TC`  Code List Table
- `TI`  Interface Table
- `TS`  System Administration Table
- `TL`  Log Table
- `TG`  Staging Table
- `TD`  Dimension Table
- `TF`  Fact Table
- `TU`  User Maintained Table

### Table Name Suffix
- `HS`  History Data Table