package controller;

import model.Book;
import model.Category;
import service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/book/*")
public class BookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định là list nếu không có action
        }

        switch (action) {
            case "list":
                listBooks(req, resp);
                break;
            case "add":
                showAddForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "filter":
                filterBooks(req, resp);
                break;
            default:
                listBooks(req, resp); // Fallback to list
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            addBook(req, resp);
        } else if ("update".equals(action)) {
            updateBook(req, resp);
        } else {
            listBooks(req, resp);
        }
    }

    private void listBooks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // Fallback to page 1 if invalid
            }
        }
        try {
            List<Book> books = BookService.getAllBooks(page);
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("books", books);
            req.setAttribute("categories", categories);
            req.setAttribute("totalPages", BookService.getTotalPages(null, null, null, null));
            req.setAttribute("currentPage", page);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving books", e);
        }
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving categories", e);
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }
        try {
            long id = Long.parseLong(idParam);
            Book book = BookService.getBookById(id);
            if (book == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("book", book);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/editBook.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Book ID");
        } catch (SQLException e) {
            throw new ServletException("Error retrieving book", e);
        }
    }

    private void addBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = new Book();
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setPublisher(req.getParameter("publisher"));
        book.setCategoryId(Integer.parseInt(req.getParameter("category_id")));
        book.setStock(Integer.parseInt(req.getParameter("stock")));
        book.setOriginalPrice(Double.parseDouble(req.getParameter("original_price")));
        book.setDiscount_rate(Integer.parseInt(req.getParameter("discount_rate")));
        book.setthumbnailUrl(req.getParameter("thumbnail_url"));
        book.setDescription(req.getParameter("description"));
        book.setPublishYear(req.getParameter("publish_year") != null && !req.getParameter("publish_year").isEmpty()
                ? Integer.parseInt(req.getParameter("publish_year")) : null);
        book.setPages(req.getParameter("pages") != null && !req.getParameter("pages").isEmpty()
                ? Integer.parseInt(req.getParameter("pages")) : null);
        book.setRating(Double.parseDouble(req.getParameter("rating_average")));
        book.setPrice(Double.parseDouble(req.getParameter("price")));

        try {
            BookService.addBook(book);
            resp.sendRedirect(req.getContextPath() + "/admin/book?action=list");
        } catch (SQLException e) {
            throw new ServletException("Error adding book", e);
        }
    }

    private void updateBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }
        try {
            long id = Long.parseLong(idParam);
            Book book = new Book();
            book.setId((int) id);
            book.setTitle(req.getParameter("title"));
            book.setAuthor(req.getParameter("author"));
            book.setPublisher(req.getParameter("publisher"));
            book.setCategoryId(Integer.parseInt(req.getParameter("category_id")));
            book.setStock(Integer.parseInt(req.getParameter("stock")));
            book.setOriginalPrice(Double.parseDouble(req.getParameter("original_price")));
            book.setDiscount_rate(Integer.parseInt(req.getParameter("discount_rate")));
            book.setthumbnailUrl(req.getParameter("thumbnail_url"));
            book.setDescription(req.getParameter("description"));
            book.setPublishYear(req.getParameter("publish_year") != null && !req.getParameter("publish_year").isEmpty()
                    ? Integer.parseInt(req.getParameter("publish_year")) : null);
            book.setPages(req.getParameter("pages") != null && !req.getParameter("pages").isEmpty()
                    ? Integer.parseInt(req.getParameter("pages")) : null);
            book.setRating(Double.parseDouble(req.getParameter("rating_average")));
            book.setPrice(Double.parseDouble(req.getParameter("price")));

            BookService.updateBook(book);
            resp.sendRedirect(req.getContextPath() + "/admin/book?action=list");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        } catch (SQLException e) {
            throw new ServletException("Error updating book", e);
        }
    }

    private void filterBooks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        Integer publishYear = req.getParameter("publish_year") != null && !req.getParameter("publish_year").isEmpty()
                ? Integer.parseInt(req.getParameter("publish_year"))
                : null;
        List<Long> includeCategories = parseCategoryIds(req.getParameter("includeCategories"));
        List<Long> excludeCategories = parseCategoryIds(req.getParameter("excludeCategories"));
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // Fallback to page 1 if invalid
            }
        }

        try {
            List<Book> books = BookService.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("books", books);
            req.setAttribute("categories", categories);
            req.setAttribute("totalPages", BookService.getTotalPages(title, publishYear, includeCategories, excludeCategories));
            req.setAttribute("currentPage", page);
            req.setAttribute("title", title);
            req.setAttribute("publishYear", publishYear);
            req.setAttribute("includeCategories", includeCategories);
            req.setAttribute("excludeCategories", excludeCategories);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error filtering books", e);
        }
    }

    private List<Long> parseCategoryIds(String categoryParam) {
        if (categoryParam == null || categoryParam.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return Arrays.stream(categoryParam.split(","))
                    .filter(s -> !s.trim().isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu parse lỗi
        }
    }
}