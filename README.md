# Cinema Management Project

A web-based cinema management system developed with Spring Boot, Thymeleaf, Spring Security, and MySQL.

---

## Features

## User Features
- User authentication & login
- Browse movies
- View showtimes
- Select seats
- Book tickets
- Responsive UI with Thymeleaf

## Admin Features
- Manage movies
- Manage showtimes
- Manage bookings
- Manage users
- Admin dashboard

---

# Tech Stack

## Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

## Frontend
- Thymeleaf
- HTML
- CSS

## Database
- MySQL

## Build Tool
- Maven

---

# Project Structure

```text
src
├── main
│   ├── java/com/example/project_web_cinema
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   ├── service
│   │   └── ProjectWebCinemaApplication
│   │
│   └── resources
│       ├── static
│       │   └── css
│       ├── templates
│           └── html
│       ├── application.properties
│       └── application.properties.example
│
└── test
```

---

# Configuration

## Clone Repository

```bash
git clone https://github.com/your-username/cinema-management-project.git
```

---

## Setup Database

Create a MySQL database:

```sql
CREATE DATABASE cinema_management;
```

---

# Install MySQL

Download and install MySQL:

- MySQL Community Server  
  https://dev.mysql.com/downloads/mysql/

- MySQL Workbench  
  https://dev.mysql.com/downloads/workbench/

---

## Configure Application Properties

Copy:

```text
application.properties.example
```

to:

```text
application.properties
```

Then update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cinema_management
spring.datasource.username=root
spring.datasource.password=your_password
```

---

# Run Project

## Using IntelliJ IDEA
- Open project
- Load Maven dependencies
- Run:

```text
ProjectWebCinemaApplication
```

---

## Using Maven

```bash
mvn spring-boot:run
```

---

# Security

- Spring Security Authentication
- Role-based authorization
- Admin/User access control

---

# UI Pages

- Home Page
- Movies Page
- Booking Page
- Seat Selection
- Admin Dashboard

---

# Dependencies

Main dependencies used:

```xml
spring-boot-starter-web
spring-boot-starter-thymeleaf
spring-boot-starter-data-jpa
spring-boot-starter-security
mysql-connector-j
lombok
```

---

# Git Ignore

Sensitive files are excluded:

```gitignore
src/main/resources/application.properties
.idea/
target/
```

---

# Author

Developed by Nguyen Hoang Khang (leader) and my team

---
