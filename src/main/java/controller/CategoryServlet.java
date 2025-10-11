//package controller;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import constant.PathConstants;
//import model.Category;
//import service.CategoryService;
//
//@WebServlet("/admin/category")
//public class CategoryServlet extends HttpServlet {
//    private final CategoryService categoryService = new CategoryService();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setCharacterEncoding("UTF-8");
//        resp.setContentType("text/html;charset=UTF-8");
//        String action = req.getParameter("action");
//        if (action == null) {
//            action = "list";
//        }
//
//        HttpSession session = req.getSession();
//        String message = (String) session.getAttribute("message");
//        if (message != null) {
//            req.setAttribute("message", message);
//            session.removeAttribute("message");
//        }
//
//        try {
//            switch (action) {
//                case "checkName": {
//                    String name = req.getParameter("name");
//                    boolean exists = categoryService.isCategoryNameExists(name);
//                    resp.setContentType("application/json");
//                    resp.getWriter().write("{\"exists\":" + exists + "}");
//                    break;
//                }
//                case "add": {
//                    List<String> categoryNames = categoryService.getAllCategories().stream()
//                            .map(Category::getName)
//                            .collect(Collectors.toList());
//                    req.setAttribute("categoryNames", categoryNames);
//                    req.setAttribute("allCategories", categoryService.getAllCategories());
//                    req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/addCategory.jsp");
//                    RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
//                    dispatcher.forward(req, resp);
//                    break;
//                }
//                case "edit": {
//                    Long editId = Long.parseLong(req.getParameter("id"));
//                    Category category = categoryService.getCategoryById(editId);
//                    req.setAttribute("category", category);
//                    req.setAttribute("allCategories", categoryService.getAllCategories());
//                    req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/editCategory.jsp");
//                    RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
//                    dispatcher.forward(req, resp);
//                    break;
//                }
//                case "list":
//                default: {
//                    List<Category> categories = categoryService.getAllCategories();
//                    req.setAttribute("categories", categories);
//                    req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/manageCategory.jsp");
//                    RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
//                    dispatcher.forward(req, resp);
//                    break;
//                }
//            }
//        } catch (RuntimeException e) {
//            session.setAttribute("message", "Error: " + e.getMessage());
//            resp.sendRedirect(req.getContextPath() + "/admin/category?action=list");
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setCharacterEncoding("UTF-8");
//        resp.setContentType("text/html;charset=UTF-8");
//        String action = req.getParameter("action");
//        if (action == null) {
//            action = "";
//        }
//
//        HttpSession session = req.getSession();
//        String message;
//
//        try {
//            switch (action) {
//                case "create": {
//                    Category category = new Category();
//                    category.setName(req.getParameter("name"));
//                    String parentIdStr = req.getParameter("parent_id");
//                    if (parentIdStr != null && !parentIdStr.isEmpty()) {
//                        category.setParent(categoryService.getCategoryById(Long.parseLong(parentIdStr)));
//                    }
//                    category.setIsLeaf("true".equals(req.getParameter("is_leaf")));
//                    categoryService.createCategory(category);
//                    message = "Thêm category thành công.";
//                    break;
//                }
//                case "update": {
//                    Category category = categoryService.getCategoryById(Long.parseLong(req.getParameter("id")));
//                    category.setName(req.getParameter("name"));
//                    String parentIdStr = req.getParameter("parent_id");
//                    if (parentIdStr != null && !parentIdStr.isEmpty()) {
//                        category.setParent(categoryService.getCategoryById(Long.parseLong(parentIdStr)));
//                    } else {
//                        category.setParent(null);
//                    }
//                    category.setIsLeaf("true".equals(req.getParameter("is_leaf")));
//                    categoryService.updateCategory(category);
//                    message = "Cập nhật category thành công.";
//                    break;
//                }
//                case "delete": {
//                    Long deleteId = Long.parseLong(req.getParameter("id"));
//                    categoryService.deleteCategory(deleteId);
//                    message = "Xóa category thành công.";
//                    break;
//                }
//                default:
//                    message = "Hành động không hợp lệ.";
//            }
//        } catch (RuntimeException e) {
//            message = "Lỗi: " + e.getMessage();
//        }
//
//        session.setAttribute("message", message);
//        resp.sendRedirect(req.getContextPath() + "/admin/category?action=list");
//    }
//}