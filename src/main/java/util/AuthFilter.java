package util;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.PathConstants;
//import dao.UserDao;
import model.User;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        String path = req.getRequestURI().substring(req.getContextPath().length());
//
//        String access_token = null;
//        String refresh_token = null;
//
//        if (req.getCookies() != null) {
//            for (Cookie c : req.getCookies()) {
//                if ("access_token".equals(c.getName()))
//                    access_token = c.getValue();
//                if ("refresh_token".equals(c.getName()))
//                    refresh_token = c.getValue();
//            }
//        }
//
//        boolean loggedIn = false;
//        String email = null;
//        String role = null;
//
//        // Check access token
//        if (access_token != null && JwtUtil.validateToken(access_token)) {
//            loggedIn = true;
//            email = JwtUtil.getEmail(access_token);
//            role = JwtUtil.getRole(access_token);
//        }
//        // Nếu access token hết hạn, dùng refresh token cấp mới
//        else if (refresh_token != null && JwtUtil.validateToken(refresh_token)
//                && JwtUtil.isRefreshToken(refresh_token)) {
//            email = JwtUtil.getEmail(refresh_token);
//            role = JwtUtil.getRole(refresh_token);
//            access_token = JwtUtil.generateAccessToken(email, role);
//
//            Cookie newAccessCookie = new Cookie("access_token", access_token);
//            newAccessCookie.setHttpOnly(true);
//            newAccessCookie.setPath("/");
//            newAccessCookie.setMaxAge(15 * 60); // 15 phút
//            res.addCookie(newAccessCookie);
//            loggedIn = true;
//        }
//
//        // Kiểm tra phân quyền
//        if (!loggedIn) {
//            // Yêu cầu login để truy cập tính năng của user
//            if (path.startsWith("/user")) {
//                request.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
//                request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
//                return;
//            }
//            // Bảo vệ các url của admin
//            if (path.startsWith("/admin")) {
//                request.setAttribute("contentPage", PathConstants.VIEW_NOT_FOUND);
//                request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
//                return;
//            }
//        } else {
//            // User đã login
//            HttpSession session = req.getSession(false); // lấy session hiện có, nếu chưa có thì trả về null
//            if (session == null || session.getAttribute("user") == null) {
//
//                Optional<User> user = new UserDao().findByEmail(email);
//
//                user.ifPresent(us -> {
//                    HttpSession newSession = req.getSession(true); // tạo mới nếu cần
//                    newSession.setAttribute("user", us.safeUser());
//                });
//
//            }
//            if (path.startsWith("/admin") && !"admin".equals(role)) {
//                request.getRequestDispatcher(PathConstants.VIEW_NOT_FOUND).forward(request, response);
//                return;
//            }
//        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
