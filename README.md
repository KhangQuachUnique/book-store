# Book Store Backend Documentation

## Overview
This is the backend project for a Book Store management system, built with Java Servlet, Maven, and PostgreSQL. It provides APIs for managing books and users, and serves JSP pages for the web interface.

## Features
- Book management (CRUD)
- User management (CRUD)
- Authentication (JWT-based)
- Serves static assets and JSP views

## Technologies Used
- Java Servlet
- Maven
- PostgreSQL
- JSP

## Project Structure
```
src/
  main/
    java/
      constant/      # Path and API constants
      controller/    # Servlets handling requests
      dao/           # Database access objects
      model/         # Data models (Book, User)
      service/       # Business logic (not yet implemented)
      util/          # Utilities (DBConnection, JwtUtil)
    webapp/
      assets/        # Static resources (images, js, css)
      WEB-INF/
        views/       # JSP views
```

## Setup
1. Clone the repository.
2. Configure PostgreSQL connection in `DBConnection.java`.
3. Build with Maven: `mvn clean package`
4. Deploy the generated WAR file to your servlet container (e.g., Tomcat).

## Usage
- Access API endpoints via `/api/*`.
- Access web pages via root URL.

## Contact
For questions or support, please contact the project maintainer.
