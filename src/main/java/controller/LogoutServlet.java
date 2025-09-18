package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();

        // Xóa cookie access_token & refresh_token bằng cách set maxAge=0
        javax.servlet.http.Cookie access = new javax.servlet.http.Cookie("access_token", "");
        access.setPath("/");
        access.setHttpOnly(true);
        access.setMaxAge(0); // xóa ngay
        resp.addCookie(access);

        javax.servlet.http.Cookie refresh = new javax.servlet.http.Cookie("refresh_token", "");
        refresh.setPath("/");
        refresh.setHttpOnly(true);
        refresh.setMaxAge(0);
        resp.addCookie(refresh);

        HttpSession session = req.getSession(false); // lấy session nếu tồn tại, không tạo mới
        if (session != null) {
            session.invalidate(); // xóa toàn bộ session và attributes
        }
        // Nếu bạn lưu refresh token trong DB/Redis thì xóa ở đây (chưa triển khai)
        resp.getWriter().print(gson.toJson(new Response("Logout successful")));
    }

    @SuppressWarnings("unused")
    private static class Response {
        String message;

        Response(String msg) {
            this.message = msg;
        }
    }
}
