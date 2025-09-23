package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;
import service.UserService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try (BufferedReader reader = req.getReader()) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            String name = json.has("name") ? json.get("name").getAsString() : null;
            String email = json.get("email").getAsString();
            String password = json.get("password").getAsString();
            String phone = json.has("phone") ? json.get("phone").getAsString() : null;

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);

            // ⚡ Đăng ký + tạo verify token trong DB
            String verifyToken = userService.register(user, password);

            JsonObject res = new JsonObject();

            if (verifyToken == null) {
                // email exists or invalid
                res.addProperty("error", "Email already exists or invalid!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(res));
                return;
            }

            // Nếu thành công → gửi mail xác thực
            try {
                String verifyLink = req.getRequestURL().toString().replace("/register", "")
                        + "/verify?token=" + verifyToken;

                util.SendMailUtil.sendVerificationMail(email, verifyLink);

                res.addProperty("message", "Register success, please check email to verify!");
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception mailErr) {
                res.addProperty("error", "User created but email could not be sent: " + mailErr.getMessage());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
