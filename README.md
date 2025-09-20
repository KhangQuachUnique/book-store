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

---

## 3. Công nghệ sử dụng

- **Java Servlet API 4.0.1**
- **JSTL 1.2**
- **Taglibs Standard 1.1.2**
- **PostgreSQL JDBC Driver 42.6.0**
- **Maven** (quản lý phụ thuộc và build)
- **JSP** cho giao diện

---

## 4. Database

- Sử dụng PostgreSQL.
- Kết nối qua JDBC, thông tin cấu hình trong `DBConnection.java`.
- Bảng mẫu: `books` (id, title, author, price).

---

## 5. Các thành phần chính

### 5.1. Controller

- `BookServlet`: Xử lý API `/api/book` (GET trả về danh sách sách dạng JSON).
- `UserServlet`: Chuẩn bị cho API `/api/user` (chưa triển khai logic).

### 5.2. DAO

- `BookDao`: Truy vấn danh sách sách từ DB.
- `UserDao`: Chưa triển khai.

### 5.3. Model

- `Book`: Định nghĩa đối tượng sách.
- `User`: Chưa triển khai thuộc tính.

### 5.4. Service

- `BookService`, `UserService`: Chưa có logic, để mở rộng nghiệp vụ.

### 5.5. Util

- `DBConnection`: Quản lý kết nối DB.
- `JwtUtil`: Để mở rộng xác thực JWT (chưa triển khai).

### 5.6. Constant

- `PathConstants`: Định nghĩa các đường dẫn API và view.

---

## 6. Cấu hình Servlet (web.xml)

- Định nghĩa servlet cho Book và User.
- Mapping URL `/api/book` và `/api/user`.
- Welcome file: `layout.jsp`.

---

## 7. Hướng dẫn chạy project

1. **Cài đặt PostgreSQL** và tạo bảng `books` với các trường: id, title, author, price.
2. **Cập nhật thông tin kết nối DB** trong `DBConnection.java` nếu cần.
3. **Build project** bằng Maven:
   ```
   mvn clean package
   ```
4. **Triển khai file WAR** lên server Tomcat hoặc môi trường hỗ trợ Java Servlet.
5. **Truy cập API**:
   - GET `/api/book`: Lấy danh sách sách (JSON).
   - Các API khác: cần bổ sung logic.

---

## 8. Mở rộng

- Thêm logic cho User (model, dao, service, controller).
- Bổ sung xác thực JWT.
- Viết unit test cho các lớp DAO, Service.
- Hoàn thiện giao diện JSP.

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
