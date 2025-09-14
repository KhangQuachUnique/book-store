package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/admin/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String token = null;

        // Lấy token từ cookie
        if (req.getCookies() != null)
            for (Cookie c : req.getCookies())
                if ("access_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }

        if (token == null || !JwtUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Missing or invalid token\"}");
            return;
        }

        // Lấy role từ token
        String role = JwtUtil.getRoleFromToken(token);

        // Chỉ cho ADMIN vào /admin/*
        if (!"admin".equalsIgnoreCase(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Access denied\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
