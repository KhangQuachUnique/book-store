package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.User;
import model.LoginResult;
import service.UserService;
import util.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try (BufferedReader reader = req.getReader()) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            String email = json.get("email").getAsString();
            String password = json.get("password").getAsString();

            LoginResult result = userService.login(email, password);

            JsonObject res = new JsonObject();

            switch (result.getStatus()) {
                case INVALID:
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.addProperty("error", "Invalid email or password");
                    break;

                case BLOCKED:
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.addProperty("error", "Account is blocked");
                    Timestamp until = result.getBlockedUntil();
                    if (until != null) {
                        res.addProperty("blockedUntil", until.toString());
                    }
                    break;

                case SUCCESS:
                    User user = result.getUser();
                    String accessToken = JwtUtil.generateAccessToken(user.getEmail());
                    String refreshToken = JwtUtil.generateRefreshToken(user.getEmail());

                    resp.setStatus(HttpServletResponse.SC_OK);
                    res.addProperty("message", "Login success");
                    res.addProperty("accessToken", accessToken);
                    res.addProperty("refreshToken", refreshToken);
                    res.addProperty("email", user.getEmail());
                    res.addProperty("role", user.getRole());
                    break;
            }

            out.print(gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject res = new JsonObject();
            res.addProperty("error", "Server error: " + e.getMessage());
            out.print(gson.toJson(res));
        }
    }
}
