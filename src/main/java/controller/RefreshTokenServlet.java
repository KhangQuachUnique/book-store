package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RefreshTokenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try (BufferedReader reader = req.getReader()) {
            JsonObject body = JsonParser.parseReader(reader).getAsJsonObject();
            String refreshToken = body.get("refreshToken").getAsString();

            // Verify refresh token
            if (JwtUtil.validateToken(refreshToken)) {
                String email = JwtUtil.getEmailFromToken(refreshToken);

                // Cấp access token mới
                String newAccessToken = JwtUtil.generateToken(email, 15); // 15 phút

                JsonObject resJson = new JsonObject();
                resJson.addProperty("accessToken", newAccessToken);
                resJson.addProperty("message", "Token refreshed successfully");

                out.print(gson.toJson(resJson));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Invalid refresh token\"}");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
