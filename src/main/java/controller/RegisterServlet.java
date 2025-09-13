package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
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

            boolean success = userService.register(user, password);

            if (success) {
                JsonObject res = new JsonObject();
                res.addProperty("message", "Register success");
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(res));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject res = new JsonObject();
                res.addProperty("error", "Email already exists");
                out.print(gson.toJson(res));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject res = new JsonObject();
            res.addProperty("error", "Server error: " + e.getMessage());
            out.print(gson.toJson(res));
        }
    }
}
