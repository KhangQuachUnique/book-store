package controller;

import dao.CategoryDao;
import model.Category;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

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
    if (action == null) action = "list";

    // Xử lý kiểm tra trùng tên cho JS
    if ("checkName".equals(action)) {
        String name = req.getParameter("name");
        boolean exists = false;
        List<Category> categories = categoryDao.findAll();
        for (Category cat : categories) {
            if (cat.getName().equalsIgnoreCase(name)) {
                exists = true;
                break;
            }
        }
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
        case "add":
            RequestDispatcher addDispatcher =
                req.getRequestDispatcher("/WEB-INF/views/CategoryManagement/addCategory.jsp");
            addDispatcher.forward(req, resp);
            break;

        case "edit":
            Long editId = Long.parseLong(req.getParameter("id"));
            Category category = categoryDao.findById(editId);
            req.setAttribute("category", category);
            // Truyền danh sách tất cả category cho datalist
            List<Category> allCategories = categoryDao.findAll();
            req.setAttribute("allCategories", allCategories);

            RequestDispatcher editDispatcher =
                req.getRequestDispatcher("/WEB-INF/views/CategoryManagement/editCategory.jsp");
            editDispatcher.forward(req, resp);
            break;

        case "list":
        default:
            List<Category> categories = categoryDao.findAll();
            req.setAttribute("categories", categories);

            RequestDispatcher listDispatcher =
                req.getRequestDispatcher("/WEB-INF/views/CategoryManagement/manageCategory.jsp");
            listDispatcher.forward(req, resp);
            break;
    }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html; charset=UTF-8");

    String action = req.getParameter("action");
    if (action == null) action = "";

        HttpSession session = req.getSession();
        String message = "";
        try {
            switch (action) {
                case "create": {
                    String err = createCategory(req);
                    if (err == null) {
                        message = "Thêm category thành công.";
                    } else {
                        message = "Thêm category thất bại: " + err;
                    }
                    break;
                }
                case "update": {
                    String err = updateCategory(req);
                    if (err == null) {
                        message = "Cập nhật category thành công.";
                    } else {
                        message = "Cập nhật category thất bại: " + err;
                    }
                    break;
                }
                case "delete": {
                    Long deleteId = Long.parseLong(req.getParameter("id"));
                    String err = categoryDao.delete(deleteId);
                    if (err == null) {
                        message = "Xóa category thành công.";
                    } else {
                        message = "Xóa category thất bại: " + err;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }
        session.setAttribute("message", message);
        resp.sendRedirect(req.getContextPath() + "/api/category?action=list");
    }

    private String createCategory(HttpServletRequest req) {
        String name = req.getParameter("name");

        if (categoryDao.isCategoryNameExists(name)) {
            return "Tên category đã tồn tại. Vui lòng chọn tên khác.";
        }

        Category c = new Category();
        c.setName(name); // Dùng lại biến name đã lấy

        String parentIdStr = req.getParameter("parent_id");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            c.setParentId(Long.parseLong(parentIdStr));
        }

        c.setIsLeaf("true".equals(req.getParameter("is_leaf")));
        c.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return categoryDao.create(c);
    }

    private String updateCategory(HttpServletRequest req) {
        Category c = new Category();
        c.setId(Long.parseLong(req.getParameter("id")));
        c.setName(req.getParameter("name"));

        String parentIdStr = req.getParameter("parent_id");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            c.setParentId(Long.parseLong(parentIdStr));
        } else {
            c.setParentId(null);
        }

        c.setIsLeaf("true".equals(req.getParameter("is_leaf")));

        return categoryDao.update(c);
    }
}
