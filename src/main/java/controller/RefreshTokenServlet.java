package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import util.JwtUtil;

@WebServlet("/user/refresh")
public class RefreshTokenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();

        try (PrintWriter out = resp.getWriter()) {

            // Lấy refresh_token từ cookie
            String refreshToken = null;
            if (req.getCookies() != null) {
                for (Cookie cookie : req.getCookies()) {
                    if ("refresh_token".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Missing refresh token\"}");
                return;
            }

            // Validate + check token type
            if (!JwtUtil.validateToken(refreshToken) || !JwtUtil.isRefreshToken(refreshToken)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Invalid or expired refresh token\"}");
                return;
            }

            String email = JwtUtil.getEmailFromToken(refreshToken);
            String role = JwtUtil.getRoleFromToken(refreshToken);

            // Cấp access token mới có kèm role
            String newAccessToken = JwtUtil.generateAccessToken(email, role);

            // Gửi access token mới qua cookie
            Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
            newAccessCookie.setHttpOnly(true);
            newAccessCookie.setPath("/");
            newAccessCookie.setMaxAge((int) (15 * 60)); // 15 phút
            resp.addCookie(newAccessCookie);

            JsonObject resJson = new JsonObject();
            resJson.addProperty("message", "Token refreshed successfully");

            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(resJson));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"error\":\"" + e.getMessage() + "\"}");
            }
        }
    }
}
