# Book Store Backend Documentation

## 1. Tổng quan

Đây là project backend cho hệ thống quản lý sách, sử dụng Java Servlet, Maven, kết nối PostgreSQL. Project cung cấp API cho các chức năng quản lý sách và người dùng, đồng thời phục vụ các trang JSP cho giao diện web.

---

## 2. Cấu trúc thư mục

```
src/
  main/
    java/
      constant/      // Định nghĩa các hằng số về đường dẫn và API
      controller/    // Các Servlet xử lý request
      dao/           // Truy xuất dữ liệu từ DB
      model/         // Định nghĩa các model (Book, User)
      service/       // Xử lý nghiệp vụ (chưa triển khai)
      util/          // Tiện ích (DBConnection, JwtUtil)
    webapp/
      assets/        // Tài nguyên tĩnh (ảnh, js, css)
      WEB-INF/
        web.xml      // Cấu hình servlet
        views/       // Các file JSP giao diện
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

---

## 9. Lưu ý

- Không sử dụng mạng trường khi deploy (theo README).
- Thông tin DB hiện tại là demo, cần bảo mật khi deploy thực tế.

---
