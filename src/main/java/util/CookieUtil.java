package util;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CookieUtil {

    // Lấy cookie + refresh token nếu cần
    public static String getValidAccessToken(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = getCookieValue(req, "access_token");

        if (token == null || !JwtUtil.validateToken(token)) {
            // Gọi internal refresh servlet
            req.getRequestDispatcher("/user/refresh").include(req, resp);
            token = getCookieValue(req, "access_token");
        }

        return (token != null && JwtUtil.validateToken(token)) ? token : null;
    }

    // Lấy giá trị cookie
    public static String getCookieValue(HttpServletRequest req, String name) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (name.equals(c.getName())) return c.getValue();
            }
        }
        return null;
    }
}
