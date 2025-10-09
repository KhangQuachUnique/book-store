package controller;

import constant.PathConstants;
import dao.CategoryBookDao;
import model.Book;
import model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/categories")
public class CategoryBookServlet extends HttpServlet {
    private static final int MAX_PAGE_DISPLAY = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        Integer categoryId = null;
        try {
            String categoryParam = req.getParameter("category");
            if (categoryParam != null) {
                categoryId = Integer.parseInt(categoryParam);
            }
        } catch (NumberFormatException e) {
            // Ignore parsing error
        }

        // Lấy các tham số filter từ form
        String title = req.getParameter("title");
        String includeCategoriesParam = req.getParameter("includeCategories");
        String excludeCategoriesParam = req.getParameter("excludeCategories");
        String action = req.getParameter("action"); // "title" hoặc "categories"

        List<Long> includeCategories = null;
        if (includeCategoriesParam != null && !includeCategoriesParam.isEmpty()) {
            includeCategories = new java.util.ArrayList<>();
            for (String id : includeCategoriesParam.split(",")) {
                try {
                    includeCategories.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }

        List<Long> excludeCategories = null;
        if (excludeCategoriesParam != null && !excludeCategoriesParam.isEmpty()) {
            excludeCategories = new java.util.ArrayList<>();
            for (String id : excludeCategoriesParam.split(",")) {
                try {
                    excludeCategories.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }

        // Logic exclusive dựa trên action type
        boolean hasTitle = title != null && !title.trim().isEmpty();
        boolean hasCategories = (includeCategories != null && !includeCategories.isEmpty()) || 
                               (excludeCategories != null && !excludeCategories.isEmpty());
        
        if ("title".equals(action)) {
            // User muốn search theo title -> clear categories
            includeCategories = null;
            excludeCategories = null;
            includeCategoriesParam = null;
            excludeCategoriesParam = null;
        } else if ("categories".equals(action)) {
            // User muốn filter theo categories -> clear title
            title = null;
        } else if (hasTitle && hasCategories) {
            // Fallback: nếu có cả 2 nhưng không có action, ưu tiên title search
            includeCategories = null;
            excludeCategories = null;
            includeCategoriesParam = null;
            excludeCategoriesParam = null;
        }

        List<Book> books;
        int totalPages;

        // Sử dụng BookService.filterBooks thay vì CategoryBookDao nếu có filter
        if ((title != null && !title.trim().isEmpty()) || 
            (includeCategories != null && !includeCategories.isEmpty()) ||
            (excludeCategories != null && !excludeCategories.isEmpty())) {
            try {
                books = BookService.filterBooks(title, null, includeCategories, excludeCategories, page);
                totalPages = BookService.getTotalPages(title, null, includeCategories, excludeCategories);
            } catch (SQLException e) {
                e.printStackTrace();
                books = new java.util.ArrayList<>();
                totalPages = 1;
            }
        } else if (categoryId != null) {
            books = CategoryBookDao.getBooksByCategoryId(categoryId, page);
            totalPages = CategoryBookDao.getTotalPagesByCategory(categoryId);
        } else {
            books = CategoryBookDao.getAllBook(page);
            totalPages = CategoryBookDao.getTotalPages();
        }

        // Lấy danh sách tất cả categories để hiển thị trong bảng category
        List<Category> categories = null;
        try {
            categories = BookService.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tính toán các trang sẽ hiển thị
        int[] visiblePages = calculateVisiblePages(page, totalPages);

        req.setAttribute("books", books);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("visiblePages", visiblePages);
        req.setAttribute("showFirstEllipsis", visiblePages.length > 0 && visiblePages[0] > 1);
        req.setAttribute("showLastEllipsis", visiblePages.length > 0 && visiblePages[visiblePages.length - 1] < totalPages);

        // Truyền lại các tham số filter để hiển thị trong form
        req.setAttribute("title", title);
        req.setAttribute("includeCategories", includeCategoriesParam);
        req.setAttribute("excludeCategories", excludeCategoriesParam);

        req.setAttribute("contentPage", "/WEB-INF/views/categoryBook.jsp");
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    private int[] calculateVisiblePages(int currentPage, int totalPages) {
        // Xử lý trường hợp không có trang nào hoặc totalPages không hợp lệ
        if (totalPages <= 0) {
            return new int[]{1}; // Trả về array với page 1 thay vì empty array
        }
        
        if (totalPages <= MAX_PAGE_DISPLAY) {
            int[] pages = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pages[i] = i + 1;
            }
            return pages;
        }

        if (currentPage <= 3) {
            // Đầu: 1 2 3 4 5 ... totalPages
            return new int[]{1, 2, 3, 4, 5};
        } else if (currentPage >= totalPages - 2) {
            // Cuối: 1 ... totalPages-4 totalPages-3 totalPages-2 totalPages-1 totalPages
            return new int[]{totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1, totalPages};
        } else {
            // Giữa: 1 ... currentPage-1 currentPage currentPage+1 ... totalPages
            return new int[]{currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2};
        }
    }
}
