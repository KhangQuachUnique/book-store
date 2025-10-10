package controller;

import constant.PathConstants;
import model.Category;
import service.CategoryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/adminn/category")
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null)
            action = "list";

        if ("checkName".equals(action)) {
            String name = req.getParameter("name");
            // simple existence check by trying to create query in service layer
            boolean exists = categoryService.getAll().stream()
                    .map(Category::getName)
                    .anyMatch(n -> n.equalsIgnoreCase(name));
            resp.setContentType("application/json");
            resp.getWriter().write("{\"exists\":" + exists + "}");
            return;
        }

        HttpSession session = req.getSession();
        String message = (String) session.getAttribute("message");
        if (message != null) {
            req.setAttribute("message", message);
            session.removeAttribute("message");
        }

        switch (action) {
            case "add": {
                List<String> categoryNames = categoryService.getAll().stream()
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
                Category category = categoryService.getById(editId);
                req.setAttribute("category", category);

                List<Category> allCategories = categoryService.getAll();
                req.setAttribute("allCategories", allCategories);

                req.setAttribute("contentPage", "/WEB-INF/views/CategoryManagement/editCategory.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
                dispatcher.forward(req, resp);
                break;
            }
            case "list":
            default: {
                List<Category> allCategories = categoryService.getAll();
                
                // Phân trang
                int pageSize = 15; // Số danh mục trên mỗi trang
                int totalCategories = allCategories.size();
                int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
                
                // Lấy tham số page từ request, mặc định là trang 1
                int currentPage = 1;
                String pageParam = req.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    try {
                        currentPage = Integer.parseInt(pageParam);
                        // Đảm bảo currentPage hợp lệ
                        if (currentPage < 1) {
                            currentPage = 1;
                        } else if (currentPage > totalPages && totalPages > 0) {
                            currentPage = totalPages;
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua lỗi, sử dụng giá trị mặc định là 1
                    }
                }
                
                // Tính toán chỉ số bắt đầu và kết thúc cho danh sách hiện tại
                int startIndex = (currentPage - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCategories);
                
                // Lấy danh sách danh mục cho trang hiện tại
                List<Category> currentPageCategories;
                if (startIndex < totalCategories) {
                    currentPageCategories = allCategories.subList(startIndex, endIndex);
                } else {
                    currentPageCategories = List.of(); // Trả về danh sách rỗng nếu chỉ số không hợp lệ
                }
                
                // Tính toán các giá trị cho phân trang nâng cao
                int startPage = Math.max(1, currentPage - 2);
                int endPage = Math.min(totalPages, currentPage + 2);
                boolean showFirstPage = currentPage > 3;
                boolean showLastPage = currentPage < totalPages - 2;
                boolean showEllipsisFirst = currentPage > 4;
                boolean showEllipsisLast = currentPage < totalPages - 3;
                
                // Đặt các thuộc tính cần thiết cho JSP
                req.setAttribute("categories", allCategories); // Giữ lại để tương thích với mã cũ
                req.setAttribute("currentPageCategories", currentPageCategories);
                req.setAttribute("currentPage", currentPage);
                req.setAttribute("totalPages", totalPages);
                req.setAttribute("startPage", startPage);
                req.setAttribute("endPage", endPage);
                req.setAttribute("showFirstPage", showFirstPage);
                req.setAttribute("showLastPage", showLastPage);
                req.setAttribute("showEllipsisFirst", showEllipsisFirst);
                req.setAttribute("showEllipsisLast", showEllipsisLast);
                
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
                    String err = handleCreate(req);
                    message = (err == null)
                            ? "Thêm category thành công."
                            : "Thêm category thất bại: " + err;
                    break;
                }
                case "update": {
                    String err = handleUpdate(req);
                    message = (err == null)
                            ? "Cập nhật category thành công."
                            : "Cập nhật category thất bại: " + err;
                    break;
                }
                case "delete": {
                    Long deleteId = Long.parseLong(req.getParameter("id"));
                    String err = categoryService.delete(deleteId);
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
        resp.sendRedirect(req.getContextPath() + "/adminn/category?action=list");
    }

    private String handleCreate(HttpServletRequest req) {
        String name = req.getParameter("name");
        String parentIdStr = req.getParameter("parent_id");
        Long parentId = (parentIdStr != null && !parentIdStr.isEmpty()) ? Long.parseLong(parentIdStr) : null;
        Boolean isLeaf = "true".equals(req.getParameter("is_leaf"));
        return categoryService.create(name, parentId, isLeaf);
    }

    private String handleUpdate(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String parentIdStr = req.getParameter("parent_id");
        Long parentId = (parentIdStr != null && !parentIdStr.isEmpty()) ? Long.parseLong(parentIdStr) : null;
        Boolean isLeaf = "true".equals(req.getParameter("is_leaf"));
        return categoryService.update(id, name, parentId, isLeaf);
    }
}
