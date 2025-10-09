//package controller;
//
//import static util.CookieUtil.getCookieValue;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import util.JwtUtil;
//
//@WebServlet("/user/refresh")
//public class RefreshTokenServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json;charset=UTF-8");
//        Gson gson = new Gson();
//
//        String refreshToken = getCookieValue(req, "refresh_token");
//        boolean isInclude = req.getAttribute("javax.servlet.include.request_uri") != null;
//
//        if (refreshToken == null || !JwtUtil.validateToken(refreshToken) || !JwtUtil.isRefreshToken(refreshToken)) {
//            if (!isInclude) {
//                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                resp.getWriter().print("{\"error\":\"Missing or invalid refresh token\"}");
//            }
//            return;
//        }
//
//        String email = JwtUtil.getEmail(refreshToken);
//        String role = JwtUtil.getRole(refreshToken);
//        String newAccessToken = JwtUtil.generateAccessToken(email, role);
//
//        Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
//        newAccessCookie.setHttpOnly(true);
//        newAccessCookie.setPath("/");
//        newAccessCookie.setMaxAge(60 * 60); // 60 phút
//        resp.addCookie(newAccessCookie);
//
//        if (!isInclude) {
//            JsonObject resJson = new JsonObject();
//            resJson.addProperty("message", "Token refreshed successfully");
//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().print(gson.toJson(resJson));
//        }
//    }
//}
