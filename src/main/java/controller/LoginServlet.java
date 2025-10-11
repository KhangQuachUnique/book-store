package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.LoginResult;
import model.User;
import service.UserService;
import util.JwtUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        try (var out = resp.getWriter()) {

            JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
            
            // Validate input data
            if (!json.has("email") || !json.has("password")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject res = new JsonObject();
                res.addProperty("error", "Email and password are required");
                out.print(gson.toJson(res));
                return;
            }
            
            String email = json.get("email").getAsString().trim();
            String password = json.get("password").getAsString();

            // Basic validation
            if (email.isEmpty() || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject res = new JsonObject();
                res.addProperty("error", "Email and password cannot be empty");
                out.print(gson.toJson(res));
                return;
            }

            LoginResult result = userService.login(email.toLowerCase(), password);
            JsonObject res = new JsonObject();

            switch (result.getStatus()) {
                case INVALID -> {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.addProperty("error", "Invalid email or password");
                }
                case UNVERIFIED -> {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.addProperty("error", "Account not verified! Please check email to verify!");
                    String verifyLink = req.getRequestURL().toString().replace("/login", "")
                            + "/verify?token=" + result.getUser().getVerifyToken();
                    util.SendMailUtil.sendVerificationMail(email, verifyLink);
                }
                case BLOCKED -> {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.addProperty("error", "Account is blocked");
                    if (result.getBlockedUntil() != null)
                        res.addProperty("blockedUntil", result.getBlockedUntil().toString());
                }
                case SUCCESS -> {
                    User user = result.getUser();

                    // Gắn role và email vào access token
                    String accessToken = JwtUtil.generateAccessToken(user.getEmail(), user.getRole().toString());
                    String refreshToken = JwtUtil.generateRefreshToken(user.getEmail(), user.getRole().toString());

                    // Cookie cho access token (hết hạn sau 60 phút)
                    Cookie accessCookie = new Cookie("access_token", accessToken);
                    accessCookie.setHttpOnly(true);
                    accessCookie.setPath("/"); // gửi cho toàn bộ domain
                    accessCookie.setMaxAge(60 * 60); // 60 phút
                    resp.addCookie(accessCookie);

                    // Cookie cho refresh token (hết hạn sau 7 ngày)
                    Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setPath("/"); // có thể để riêng "/auth" nếu chỉ dùng cho refresh
                    refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                    resp.addCookie(refreshCookie);

                    resp.setStatus(HttpServletResponse.SC_OK);
                    res.addProperty("message", "Login success");
                    res.addProperty("email", user.getEmail());
                    res.addProperty("role", user.getRole().toString());

                    // Set session attribute for server-side auth with safe user object
                    // This ensures compatibility with JSP EL expressions and JPA entity lifecycle
                    req.getSession(true).setAttribute("user", user.safeUser());
                }
            }
            out.print(gson.toJson(res));
        } catch (jakarta.persistence.PersistenceException pe) {
            // Handle JPA/Database connection errors
            System.err.println("LoginServlet: Database error: " + pe.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject res = new JsonObject();
            res.addProperty("error", "Database connection error. Please try again later.");
            resp.getWriter().print(gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject res = new JsonObject();
            res.addProperty("error", "Server error: " + e.getMessage());
            resp.getWriter().print(gson.toJson(res));
        }
    }
}
