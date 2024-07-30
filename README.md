## Module Description

Overseas API Service ApiDocs:
https://streamnz.com/oversea/api/documents/

## Features

Entity Object Get and Set
lombok

Object properties in CamelCase, interface fields in snake_case

User Permissions
redis, SA-TOKEN

Database
mysql

Spring Cloud
consul center, config server, openfeign

## How to Start

### Local Start
    Use IDEA or Eclipse to start directly
    JVM Parameters:
    --spring.profiles.active=dev

## API Documentation

Swagger:
http://localhost:port/swagger-ui/index.html
knife4j:
http://localhost:port/doc.html

## Differences Between Swagger2 and OpenApi3.0 Annotations
    @Api -> @Tag
    @ApiIgnore -> @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
    @ApiImplicitParam -> @Parameter
    @ApiImplicitParams -> @Parameters
    @ApiModel -> @Schema
    @ApiModelProperty(hidden = true) -> @Schema(accessMode = READ_ONLY)
    @ApiModelProperty -> @Schema
    @ApiOperation(value = "foo", notes = "bar") -> @Operation(summary = "foo", description = "bar")
    @ApiParam -> @Parameter
    @ApiResponse(code = 404, message = "foo") -> @ApiResponse(responseCode = "404", description = "foo")

### References:
- lombok:
  - Installation: https://blog.csdn.net/zhglance/article/details/54931430
  - Features: https://projectlombok.org/features/all

- swagger2:
  - https://www.cnblogs.com/magicalSam/p/7197533.html

- Alibaba Code IDEA Plugin:
  - Search for Alibaba Code Guideline in IDEA plugins and install
  - https://github.com/alibaba/p3c

## Development Conventions

Exception Handling
Only handle exceptions that need to be dealt with by the business logic.
Do not use try-catch blocks without any actions; ensure e.printStackTrace or log.error is used to record the exceptions.

Transaction Handling
When using @Transactional, avoid catching exceptions within the method as it prevents rollback.
By default, only runtime exceptions trigger rollback. To rollback on specific exceptions or all exceptions, use rollbackFor.
Example: rollbackFor = Exception.class

Error Messages
All error messages must be configured in messages.properties. Do not include any Chinese messages directly in the code.

Constant Definitions
All constants must be defined in SystemConstants.

Avoid Mapping Unnecessary Fields
If there is no specific operation needed, avoid mapping timestamp fields to the entity and use the database's SYSDATE instead.

Use Rich Model
Add business validations and logic in entity objects to implement a rich model.
Utilize collection streams to filter data in the code layer to reduce database interactions.

## DON'T DO THIS
    Avoid using reflection in the code unless absolutely necessary.
    Avoid using threads in the code unless absolutely necessary.
    Do not use magic numbers in the business logic, such as if (i == 1).
    For file attachments, since the system uses an open-source S3 implementation (Minio), do not store files locally or on server disks.

## Table Name Conventions

### Table Name Prefix
    TM  Master Data Table
    TT  Transaction Data Table
    TR  Relationship Table
    TA  Aggregation Table
    TC  Code List Table
    TI  Interface Table
    TS  System Administration Table
    TL  Log Table
    TG  Staging Table
    TD  Dimension Table
    TF  Fact Table
    TU  User Maintained Table

### Table Name Suffix
    HS  History Data Table
