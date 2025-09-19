package controller;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.Book;
import model.Category;
import service.BookService;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;


/**
 * Servlet for managing book-related operations (CRUD, filter, CSV import).
 */
@WebServlet("/admin/book")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class BookServlet extends HttpServlet {
    private final BookService service = new BookService();

    /**
     * Handles GET requests (list, view, edit, add, filter).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        try {
            switch (action) {
                case "list":
                    listBooks(req, resp);
                    break;
                case "view":
                    viewBook(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "filter":
                    filterBooks(req, resp);
                    break;
                default:
                    listBooks(req, resp);
            }
        } catch (SQLException e) {
            handleException(req, resp, e);
        }
    }

    /**
     * Handles POST requests (add, update, delete, importCSV).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addBook(req, resp);
                    break;
                case "update":
                    updateBook(req, resp);
                    break;
                case "delete":
                    deleteBook(req, resp);
                    break;
                case "importCSV":
                    importCSV(req, resp);
                    break;
                default:
                    listBooks(req, resp);
            }
        } catch (SQLException | CsvValidationException e) {
            handleException(req, resp, e);
        }
    }

    /**
     * Lists books with pagination.
     */
    private void listBooks(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int page = getPageParameter(req);
        List<Book> books = service.getAllBooks(page);
        int totalPages = service.getTotalPages(null, null, null, null);
        List<Category> categories = service.getAllCategories();
        setPaginationAttributes(req, page, totalPages);
        req.setAttribute("books", books);
        req.setAttribute("categories", categories);
        req.setAttribute("listType", "All Books");
        forwardToList(req, resp);
    }

    /**
     * Displays book details.
     */
    private void viewBook(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        long id = parseLongParameter(req.getParameter("id"), resp);
        if (id == -1) return;
        Book book = service.getBookById(id);
        List<Category> categories = service.getAllCategories();
        req.setAttribute("book", book);
        req.setAttribute("categories", categories);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/viewBook.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Shows the form to add a new book.
     */
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        List<Category> categories = service.getAllCategories();
        req.setAttribute("categories", categories);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/addBook.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Shows the form to edit a book.
     */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        long id = parseLongParameter(req.getParameter("id"), resp);
        if (id == -1) return;
        Book book = service.getBookById(id);
        List<Category> categories = service.getAllCategories();
        req.setAttribute("book", book);
        req.setAttribute("categories", categories);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/editBook.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Adds a new book.
     */
    private void addBook(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Book book = extractBookFromRequest(req);
        try {
            service.addBook(book);
            resp.sendRedirect("/admin/book?action=list");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showAddForm(req, resp);
        }
    }

    /**
     * Updates an existing book.
     */
    private void updateBook(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Book book = extractBookFromRequest(req);
        book.setId((int) parseLongParameter(req.getParameter("id"), resp));
        if (book.getId() == -1) return;
        try {
            service.updateBook(book);
            resp.sendRedirect("/admin/book?action=list");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showEditForm(req, resp);
        }
    }

    /**
     * Deletes a book.
     */
    private void deleteBook(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        long id = parseLongParameter(req.getParameter("id"), resp);
        if (id == -1) return;
        service.deleteBook(id);
        resp.sendRedirect("/admin/book?action=list");
    }

    /**
     * Imports books from a CSV file.
     */
    private void importCSV(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, SQLException, CsvValidationException {
        Part filePart = req.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {
            req.setAttribute("errorMessage", "No file uploaded");
            listBooks(req, resp);
            return;
        }
        String error = service.importBooksFromCSV(filePart.getInputStream());
        if (error != null) {
            req.setAttribute("errorMessage", error);
        } else {
            req.setAttribute("successMessage", "Books imported successfully");
        }
        listBooks(req, resp);
    }

    /**
     * Filters books by criteria.
     */
    private void filterBooks(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int page = getPageParameter(req);
        String title = req.getParameter("title");
        Integer publishYear = req.getParameter("publishYear") != null && !req.getParameter("publishYear").isEmpty() ?
                Integer.parseInt(req.getParameter("publishYear")) : null;
        List<Long> includeCategories = parseCategoryIds(req.getParameterValues("includeCategories"));
        List<Long> excludeCategories = parseCategoryIds(req.getParameterValues("excludeCategories"));

        List<Book> books = service.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
        int totalPages = service.getTotalPages(title, publishYear, includeCategories, excludeCategories);
        List<Category> categories = service.getAllCategories();

        setPaginationAttributes(req, page, totalPages);
        req.setAttribute("books", books);
        req.setAttribute("categories", categories);
        req.setAttribute("listType", "Filtered Books");
        req.setAttribute("title", title);
        req.setAttribute("publishYear", publishYear);
        req.setAttribute("includeCategories", includeCategories);
        req.setAttribute("excludeCategories", excludeCategories);
        forwardToList(req, resp);
    }

    /**
     * Forwards to the book list page.
     */
    private void forwardToList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/Bookmanagement/bookList.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Gets the page number from request.
     * @return The page number (default 1).
     */
    private int getPageParameter(HttpServletRequest req) {
        String pageStr = req.getParameter("page");
        try {
            int page = Integer.parseInt(pageStr);
            return page > 0 ? page : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Sets pagination attributes for JSP.
     */
    private void setPaginationAttributes(HttpServletRequest req, int currentPage, int totalPages) {
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
    }

    /**
     * Parses a long parameter from request.
     * @return The parsed value or -1 if invalid.
     */
    private long parseLongParameter(String param, HttpServletResponse resp) throws IOException {
        if (param == null || param.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter");
            return -1;
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return -1;
        }
    }

    /**
     * Extracts a Book object from request parameters.
     */
    private Book extractBookFromRequest(HttpServletRequest req) {
        Book book = new Book();
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setPublisher(req.getParameter("publisher"));
        book.setCategoryId(req.getParameter("category_id") != null && !req.getParameter("category_id").isEmpty() ?
                Integer.parseInt(req.getParameter("category_id")) : 0);
        book.setStock(req.getParameter("stock") != null && !req.getParameter("stock").isEmpty() ?
                Integer.parseInt(req.getParameter("stock")) : 0);
        book.setOriginalPrice(req.getParameter("original_price") != null && !req.getParameter("original_price").isEmpty() ?
                Double.parseDouble(req.getParameter("original_price")) : 0.0);
        book.setDiscount_rate(req.getParameter("discount_rate") != null && !req.getParameter("discount_rate").isEmpty() ?
                Integer.parseInt(req.getParameter("discount_rate")) : 0);
        book.setthumbnailUrl(req.getParameter("thumbnail_url"));
        book.setDescription(req.getParameter("description"));
        book.setPublishYear(req.getParameter("publish_year") != null && !req.getParameter("publish_year").isEmpty() ?
                Integer.parseInt(req.getParameter("publish_year")) : null);
        book.setPages(req.getParameter("pages") != null && !req.getParameter("pages").isEmpty() ?
                Integer.parseInt(req.getParameter("pages")) : null);
        book.setRating(req.getParameter("rating_average") != null && !req.getParameter("rating_average").isEmpty() ?
                Double.parseDouble(req.getParameter("rating_average")) : 0.0);
        book.setPrice(req.getParameter("price") != null && !req.getParameter("price").isEmpty() ?
                Double.parseDouble(req.getParameter("price")) : 0.0);
        return book;
    }

    /**
     * Parses category IDs from request parameters.
     */
    private List<Long> parseCategoryIds(String[] ids) {
        if (ids == null) return new ArrayList<>();
        return Arrays.stream(ids)
                .filter(id -> !id.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * Handles exceptions by forwarding to list page with error message.
     */
    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception ex) throws ServletException, IOException {
        req.setAttribute("errorMessage", "An error occurred: " + ex.getMessage());
        forwardToList(req, resp);
    }
}