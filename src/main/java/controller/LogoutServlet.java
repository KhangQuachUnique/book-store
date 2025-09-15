// package controller;

// import com.google.gson.Gson;

// import javax.servlet.ServletException;

// import javax.servlet.http.*;
// import java.io.IOException;
// import java.io.PrintWriter;

// public class LogoutServlet extends HttpServlet {

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         resp.setContentType("application/json");
//         PrintWriter out = resp.getWriter();
//         Gson gson = new Gson();

//         // Nếu lưu refresh token trong DB/Redis → xóa tại đây
//         // Demo: chỉ báo cho client tự xoá token localStorage
//         out.print(gson.toJson(new Response("Logout successful")));
//     }

//     private static class Response {
//         String message;
//         Response(String msg) { this.message = msg; }
//     }
// }
