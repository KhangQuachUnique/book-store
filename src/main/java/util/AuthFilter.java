package util;

import constant.PathConstants;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        String access_token = null;
        String refresh_token = null;

        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("access_token".equals(c.getName())) access_token = c.getValue();
                if ("refresh_token".equals(c.getName())) refresh_token = c.getValue();
            }
        }

        boolean loggedIn = false;
        String email = null;
        String role = null;

        // Check access token
        if (access_token != null && JwtUtil.validateToken(access_token)) {
            loggedIn = true;
            email = JwtUtil.getEmailFromToken(access_token);
            role = JwtUtil.getRoleFromToken(access_token);
        }
        // Nếu access token hết hạn, dùng refresh token cấp mới
        else if (refresh_token != null && JwtUtil.validateToken(refresh_token) && JwtUtil.isRefreshToken(refresh_token)) {
            email = JwtUtil.getEmailFromToken(refresh_token);
            role = JwtUtil.getRoleFromToken(refresh_token);
            access_token = JwtUtil.generateAccessToken(email, role);

            Cookie newAccessCookie = new Cookie("access_token", access_token);
            newAccessCookie.setPath(req.getContextPath());
            newAccessCookie.setMaxAge(15 * 60); // 15 phút
            res.addCookie(newAccessCookie);
            loggedIn = true;
        }

        // Kiểm tra phân quyền
        if (!loggedIn) {
            if (path.startsWith("/user") || path.startsWith("/admin")) {
                request.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
                request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
                return;
            }
        } else {
            if (path.startsWith("/admin") && !"admin".equals(role)) {
                request.setAttribute("contentPage", PathConstants.VIEW_NOT_FOUND);
                request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
