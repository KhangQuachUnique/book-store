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


## 5. Giải thích chi tiết các folder chính

### 5.1. constant/

Chứa các file định nghĩa hằng số, chủ yếu là các đường dẫn API, đường dẫn view JSP, giúp quản lý và sử dụng nhất quán trong toàn project.

### 5.2. controller/

Chứa các Servlet, là nơi tiếp nhận và xử lý các HTTP request từ client (trình duyệt hoặc API). Mỗi Servlet sẽ đảm nhận một chức năng như quản lý sách, người dùng...

### 5.3. dao/

Chứa các lớp truy xuất dữ liệu (Data Access Object), thực hiện các thao tác với database như lấy danh sách, thêm, sửa, xóa dữ liệu.

### 5.4. model/

Chứa các lớp mô hình dữ liệu (entity), đại diện cho các đối tượng trong hệ thống như Book, User. Các class này thường chứa thuộc tính và phương thức liên quan đến dữ liệu.

### 5.5. service/

Chứa các lớp xử lý nghiệp vụ, logic phức tạp, tách biệt khỏi controller và dao. Hiện tại chưa triển khai, nhưng sẽ dùng để xử lý các quy trình nghiệp vụ như xác thực, tính toán...

### 5.6. util/

Chứa các lớp tiện ích dùng chung cho toàn project, ví dụ như quản lý kết nối database (`DBConnection`), xử lý JWT (`JwtUtil`).

### 5.7. webapp/assets/

Chứa các tài nguyên tĩnh như hình ảnh, file JavaScript, CSS phục vụ cho giao diện web.

### 5.8. webapp/WEB-INF/

Chứa các file cấu hình (web.xml) và các view JSP (giao diện động). Thư mục này không public trực tiếp ra ngoài, chỉ server mới truy cập được.


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

## 10. Liên hệ & Đóng góp

- Chủ sở hữu: KhangQuachUnique
- Đóng góp: Tạo pull request hoặc liên hệ qua Github.


---
