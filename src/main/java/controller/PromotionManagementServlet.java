package controller;

import constant.PathConstants;
import model.Promotion;
import service.PromotionService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/promotions")
public class PromotionManagementServlet extends HttpServlet {

    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // Flash messages from session to request
        moveFlash(session, req, "flash_success");
        moveFlash(session, req, "flash_error");

        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) action = "list";

        switch (action) {
            case "add": {
                req.setAttribute("mode", "create");
                req.setAttribute("contentPage", "/WEB-INF/views/admin/promotion-form.jsp");
                req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                return;
            }
            case "edit": {
                String idStr = req.getParameter("id");
                try {
                    Long id = idStr != null ? Long.parseLong(idStr) : null;
                    Promotion p = promotionService.getById(id);
                    if (p == null) {
                        session.setAttribute("flash_error", "Promotion not found");
                        resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                        return;
                    }
                    req.setAttribute("promotion", p);
                    req.setAttribute("mode", "edit");
                    req.setAttribute("contentPage", "/WEB-INF/views/admin/promotion-form.jsp");
                    req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                    return;
                } catch (NumberFormatException e) {
                    session.setAttribute("flash_error", "Invalid promotion id");
                    resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                    return;
                }
            }
            case "list":
            default: {
                List<Promotion> promotions = promotionService.listAll();
                req.setAttribute("promotions", promotions);
                req.setAttribute("contentPage", "/WEB-INF/views/admin/promotion-list.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
                dispatcher.forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        if (action == null) action = "";

        switch (action) {
            case "create": {
                String code = req.getParameter("code");
                String discount = req.getParameter("discount");
                String expireAt = req.getParameter("expireAt"); // datetime-local
                String err = promotionService.create(code, discount, expireAt);
                if (err != null) {
                    req.setAttribute("error", err);
                    req.setAttribute("mode", "create");
                    req.setAttribute("backfill_code", code);
                    req.setAttribute("backfill_discount", discount);
                    req.setAttribute("backfill_expireAt", expireAt);
                    req.setAttribute("contentPage", "/WEB-INF/views/admin/promotion-form.jsp");
                    req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                    return;
                } else {
                    session.setAttribute("flash_success", "Promotion created successfully");
                    resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                    return;
                }
            }
            case "update": {
                String idStr = req.getParameter("id");
                String code = req.getParameter("code");
                String discount = req.getParameter("discount");
                String expireAt = req.getParameter("expireAt");
                try {
                    Long id = Long.parseLong(idStr);
                    String err = promotionService.update(id, code, discount, expireAt);
                    if (err != null) {
                        req.setAttribute("error", err);
                        req.setAttribute("mode", "edit");
                        Promotion p = promotionService.getById(id);
                        // backfill edited fields when validation fails
                        if (p == null) p = new Promotion();
                        p.setCode(code);
                        try { p.setDiscount(Double.parseDouble(discount)); } catch (Exception ignored) {}
                        req.setAttribute("promotion", p);
                        req.setAttribute("backfill_expireAt", expireAt);
                        req.setAttribute("contentPage", "/WEB-INF/views/admin/promotion-form.jsp");
                        req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                        return;
                    }
                    session.setAttribute("flash_success", "Promotion updated successfully");
                    resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                    return;
                } catch (NumberFormatException e) {
                    session.setAttribute("flash_error", "Invalid id");
                    resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                    return;
                }
            }
            case "delete": {
                String idStr = req.getParameter("id");
                try {
                    Long id = Long.parseLong(idStr);
                    String err = promotionService.delete(id);
                    if (err != null) session.setAttribute("flash_error", err);
                    else session.setAttribute("flash_success", "Promotion deleted successfully");
                } catch (NumberFormatException e) {
                    session.setAttribute("flash_error", "Invalid id");
                }
                resp.sendRedirect(req.getContextPath() + "/admin/promotions");
                return;
            }
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/promotions");
        }
    }

    private void moveFlash(HttpSession session, HttpServletRequest req, String key) {
        Object v = session.getAttribute(key);
        if (v != null) {
            req.setAttribute(key, v);
            session.removeAttribute(key);
        }
    }
}
