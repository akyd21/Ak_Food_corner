# Ak_Food_corner

Ak_Food_corner is a Spring Boot food delivery application with a Thymeleaf frontend, MySQL persistence, user/admin login, cart flow, checkout, payment UI, and order history.

## Features

- User registration and login
- Admin login and dashboard
- Product/menu browsing
- Shopping cart with quantity updates
- Checkout with delivery address form
- Payment page with multiple payment options
- Order confirmation and order history
- Spring Security configuration
- Swagger/OpenAPI support

## Tech Stack

- Java 21+
- Spring Boot 3.4.2
- Spring MVC / Thymeleaf
- Spring Data JPA
- Spring Security
- MySQL
- H2 for tests
- Maven

## Project Structure

- `src/main/java/com/example/demo` - application source code
- `src/main/resources/templates` - Thymeleaf pages
- `src/main/resources/static` - CSS, JavaScript, images
- `src/test/java` - tests

## Prerequisites

- JDK 21 or later
- Maven Wrapper (`mvnw` / `mvnw.cmd`)
- MySQL server running locally

## Database Setup

Create the database before running the app:

```sql
CREATE DATABASE Food_DELIVERYApplication;
```

Default database settings in `src/main/resources/application.properties`:

- URL: `jdbc:mysql://localhost:3306/Food_DELIVERYApplication`
- Username: `root`
- Password: `1234`

Update these values if your local MySQL credentials are different.

## Run Locally

### Windows

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-22"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
.\mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
export JAVA_HOME=/path/to/jdk
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Build and Test

```bash
./mvnw test
./mvnw clean compile
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

## Default Admin Login

- Email: `akadmin@gmail.com`
- Password: `admin1234`

## Notes

- The app uses Thymeleaf for server-side rendering.
- Cart and checkout pages use client-side storage for the shopping flow.
- Swagger UI is available if enabled by your local configuration.

## License

No license file is currently included.
