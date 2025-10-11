package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.PathConstants;
import dao.CategoryDao;
import model.Category;

@WebServlet("/admin/category")
public class CategoryServlet extends HttpServlet {

    private CategoryDao categoryDao;

    @Override
    public void init() {
        categoryDao = new CategoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null)
            action = "list";

        // Xử lý kiểm tra trùng tên cho JS
        if ("checkName".equals(action)) {
            String name = req.getParameter("name");
            boolean exists = categoryDao.isCategoryNameExists(name);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"exists\":" + exists + "}");
            return;
        }

        // Lấy message từ session nếu có
        HttpSession session = req.getSession();
        String message = (String) session.getAttribute("message");
        if (message != null) {
            req.setAttribute("message", message);
            session.removeAttribute("message");
        }

        switch (action) {
            case "add": {
                // Prefetch all category names for client-side duplicate checking
                List<String> categoryNames = categoryDao.findAll().stream()
                        .map(Category::getName)
                        .collect(Collectors.toList());
                req.setAttribute("categoryNames", categoryNames);
                req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/addCategory.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
                dispatcher.forward(req, resp);
                break;
            }
            case "edit": {
                Long editId = Long.parseLong(req.getParameter("id"));
                Category category = categoryDao.findById(editId);
                req.setAttribute("category", category);

                List<Category> allCategories = categoryDao.findAll();
                req.setAttribute("allCategories", allCategories);

                req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/editCategory.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
                dispatcher.forward(req, resp);
                break;
            }
            case "list":
            default: {
                List<Category> categories = categoryDao.findAll();
                req.setAttribute("categories", categories);

                req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/manageCategory.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
                dispatcher.forward(req, resp);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = req.getParameter("action");
        if (action == null)
            action = "";

        HttpSession session = req.getSession();
        String message = "";

        try {
            switch (action) {
                case "create": {
                    String err = createCategory(req);
                    message = (err == null)
                            ? "Thêm category thành công."
                            : "Thêm category thất bại: " + err;
                    break;
                }
                case "update": {
                    String err = updateCategory(req);
                    message = (err == null)
                            ? "Cập nhật category thành công."
                            : "Cập nhật category thất bại: " + err;
                    break;
                }
                case "delete": {
                    Long deleteId = Long.parseLong(req.getParameter("id"));
                    String err = categoryDao.delete(deleteId);
                    message = (err == null)
                            ? "Xóa category thành công."
                            : "Xóa category thất bại: " + err;
                    break;
                }
                default:
                    message = "Hành động không hợp lệ.";
            }
        } catch (Exception e) {
            message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }

        session.setAttribute("message", message);
        resp.sendRedirect(req.getContextPath() + "/admin/category?action=list");
    }

    private String createCategory(HttpServletRequest req) {
        String name = req.getParameter("name");

        if (categoryDao.isCategoryNameExists(name)) {
            return "Tên category đã tồn tại. Vui lòng chọn tên khác.";
        }

        Category c = new Category();
        c.setName(name);

        String parentIdStr = req.getParameter("parent_id");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            Category parent = categoryDao.findById(Long.parseLong(parentIdStr));
            c.setParent(parent);
        }

        c.setLeaf("true".equals(req.getParameter("is_leaf")));
        c.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return categoryDao.create(c);
    }

    private String updateCategory(HttpServletRequest req) {
        Category c = new Category();
        c.setId(Long.parseLong(req.getParameter("id")));
        c.setName(req.getParameter("name"));

        String parentIdStr = req.getParameter("parent_id");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            Category parent = categoryDao.findById(Long.parseLong(parentIdStr));
            c.setParent(parent);
        } else {
            c.setParent(null);
        }

        c.setLeaf("true".equals(req.getParameter("is_leaf")));

        return categoryDao.update(c);
    }
}
