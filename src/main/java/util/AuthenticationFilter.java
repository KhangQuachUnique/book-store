package util;


import constant.PathConstants;
import dao.UserDao;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final Set<String> WHITELIST = Set.of(
            "/user/login",
            "/user/register",
            "/assets/",
            "/"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (isWhitelisted(path)) {
            filterChain.doFilter(req, resp);
            return;
        }

        // Lấy token hợp lệ (có refresh nếu còn)
        String token = CookieUtil.getValidAccessToken(req, resp);
        if (token == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            req.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
            return;
        }

        // Set user vào session
        new UserDao().findByEmail(JwtUtil.getEmail(token))
                .ifPresent(u -> req.getSession().setAttribute("user", u));

        filterChain.doFilter(req, resp);

        if ("/home".equals(path)) {
            if (JwtUtil.validateToken(token)) {
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByEmail(JwtUtil.getEmail(token));
                user.ifPresent(u -> req.getSession().setAttribute("user", u));
            }
            filterChain.doFilter(req, resp);
            return;
        }

        if (!JwtUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Missing or invalid token\"}");
            req.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
            return;
        }

        UserDao userDao = new UserDao();
        Optional<User> user = userDao.findByEmail(JwtUtil.getEmail(token));
        user.ifPresent(u -> req.getSession().setAttribute("user", u));

        filterChain.doFilter(req, resp);
    }

    private boolean isWhitelisted(String path) {
        // cho phép mọi path bắt đầu với /css/ , /js/ hoặc chính xác là /login, /register
        return WHITELIST.stream().anyMatch(path::startsWith);
    }
}
