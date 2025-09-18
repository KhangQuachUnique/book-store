package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import constant.PathConstants;
import dao.CategoryBookDao;
import model.Book;

@WebServlet("/categories")
public class CategoryBookServlet extends HttpServlet {
    private static final int MAX_PAGE_DISPLAY = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1)
                    page = 1;
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

        List<Book> books;
        int totalPages;

        if (categoryId != null) {
            books = CategoryBookDao.getBooksByCategoryId(categoryId, page);
            totalPages = CategoryBookDao.getTotalPagesByCategory(categoryId);
        } else {
            books = CategoryBookDao.getAllBook(page);
            totalPages = CategoryBookDao.getTotalPages();
        }

        // Tính toán các trang sẽ hiển thị
        int[] visiblePages = calculateVisiblePages(page, totalPages);

        req.setAttribute("books", books);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("visiblePages", visiblePages);
        req.setAttribute("showFirstEllipsis", visiblePages[0] > 1);
        req.setAttribute("showLastEllipsis", visiblePages[visiblePages.length - 1] < totalPages);

        req.setAttribute("contentPage", PathConstants.VIEW_CATEGORY_BOOKS);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    private int[] calculateVisiblePages(int currentPage, int totalPages) {
        if (totalPages <= MAX_PAGE_DISPLAY) {
            int[] pages = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pages[i] = i + 1;
            }
            return pages;
        }

        if (currentPage <= 3) {
            // Đầu: 1 2 3 4 5 ... totalPages
            return new int[] {1, 2, 3, 4, 5};
        } else if (currentPage >= totalPages - 2) {
            // Cuối: 1 ... totalPages-4 totalPages-3 totalPages-2 totalPages-1 totalPages
            return new int[] {totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1,
                    totalPages};
        } else {
            // Giữa: 1 ... currentPage-1 currentPage currentPage+1 ... totalPages
            return new int[] {currentPage - 2, currentPage - 1, currentPage, currentPage + 1,
                    currentPage + 2};
        }
    }
}
