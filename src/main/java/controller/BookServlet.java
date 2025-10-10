package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Category;
import service.BookService;

@WebServlet("/admin/book/*")
public class BookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
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
                case "delete":
                    deleteBook(req, resp);
                    break;
                default:
                    listBooks(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                addBook(req, resp);
            } else if ("update".equals(action)) {
                updateBook(req, resp);
            } else {
                listBooks(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        }
    }

    private void listBooks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        try {
            List<Book> books = BookService.getAllBooks(page);
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("books", books != null ? books : new ArrayList<>());
            req.setAttribute("categories", categories != null ? categories : new ArrayList<>());
            req.setAttribute("totalPages", BookService.getTotalPages(null, null, null, null));
            req.setAttribute("currentPage", page);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Error retrieving books: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        }
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("categories", categories != null ? categories : new ArrayList<>());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Error retrieving categories: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
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
            req.setAttribute("categories", categories != null ? categories : new ArrayList<>());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/editBook.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Book ID");
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Error retrieving book: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        }
    }

    private void addBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = new Book();
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setPublisher(req.getParameter("publisher"));
        try {
            book.setCategoryId(Integer.parseInt(req.getParameter("category_id")));
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid category ID");
            req.setAttribute("categories", BookService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp").forward(req, resp);
            return;
        }
        try {
            book.setStock(Integer.parseInt(req.getParameter("stock")));
            book.setOriginalPrice(Double.parseDouble(req.getParameter("original_price")));
            book.setDiscountRate(Integer.parseInt(req.getParameter("discountRate")));
            book.setPrice(Double.parseDouble(req.getParameter("price")));
            book.setAverageRating(Double.parseDouble(req.getParameter("averageRating")));
            String soldParam = req.getParameter("sold");
            book.setSold(soldParam != null && !soldParam.isEmpty() ? Integer.parseInt(soldParam) : 0);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid numeric input");
            req.setAttribute("categories", BookService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp").forward(req, resp);
            return;
        }
        book.setThumbnailUrl(req.getParameter("thumbnail_url"));
        book.setDescription(req.getParameter("description"));
        String publishYear = req.getParameter("publish_year");
        book.setPublishYear(publishYear != null && !publishYear.isEmpty() ? Integer.parseInt(publishYear) : null);
        String pages = req.getParameter("pages");
        book.setPages(pages != null && !pages.isEmpty() ? Integer.parseInt(pages) : null);

        try {
            BookService.addBook(book);
            resp.sendRedirect(req.getContextPath() + "/admin/book?action=list");
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("categories", BookService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp").forward(req, resp);
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
            book.setDiscountRate(Integer.parseInt(req.getParameter("discountRate")));
            book.setPrice(Double.parseDouble(req.getParameter("price")));
            book.setAverageRating(Double.parseDouble(req.getParameter("averageRating")));
            String soldParam = req.getParameter("sold");
            book.setSold(soldParam != null && !soldParam.isEmpty() ? Integer.parseInt(soldParam) : 0);
            book.setThumbnailUrl(req.getParameter("thumbnail_url"));
            book.setDescription(req.getParameter("description"));
            String publishYear = req.getParameter("publish_year");
            book.setPublishYear(publishYear != null && !publishYear.isEmpty() ? Integer.parseInt(publishYear) : null);
            String pages = req.getParameter("pages");
            book.setPages(pages != null && !pages.isEmpty() ? Integer.parseInt(pages) : null);

            BookService.updateBook(book);
            resp.sendRedirect(req.getContextPath() + "/admin/book?action=list");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid input data");
            req.setAttribute("book", BookService.getBookById(Long.parseLong(idParam)));
            req.setAttribute("categories", BookService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/editBook.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("book", BookService.getBookById(Long.parseLong(idParam)));
            req.setAttribute("categories", BookService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/editBook.jsp").forward(req, resp);
        }
    }

    private void deleteBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }
        try {
            long id = Long.parseLong(idParam);
            BookService.deleteBook(id);
            resp.sendRedirect(req.getContextPath() + "/admin/book?action=list");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Book ID");
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Error deleting book: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
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
                page = 1;
            }
        }

        try {
            List<Book> books = BookService.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
            List<Category> categories = BookService.getAllCategories();
            req.setAttribute("books", books != null ? books : new ArrayList<>());
            req.setAttribute("categories", categories != null ? categories : new ArrayList<>());
            req.setAttribute("totalPages",
                    BookService.getTotalPages(title, publishYear, includeCategories, excludeCategories));
            req.setAttribute("currentPage", page);
            req.setAttribute("title", title);
            req.setAttribute("publishYear", publishYear);
            req.setAttribute("includeCategories", includeCategories);
            req.setAttribute("excludeCategories", excludeCategories);
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Error filtering books: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp").forward(req, resp);
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
            return new ArrayList<>();
        }
    }
}