// package controller;

// import java.io.IOException;
// import java.sql.SQLException;
// import java.util.logging.Level;
// import java.util.logging.Logger;

// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.swing.text.View;

// import constant.PathConstants;
// import dao.BookDao;
// import model.Book;
// import model.BookReview;
// import model.User;
// import service.BookReviewService;

// /**
//  * Servlet for handling book detail page requests. Displays detailed information
//  * about a specific
//  * book including all attributes.
//  *
//  * @author BookStore Team
//  * @version 1.0
//  */
// @WebServlet("/book-detail")
// public class BookDetailServlet extends HttpServlet {

//     private static final Logger log = Logger.getLogger(BookDetailServlet.class.getName());

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         try {
//             // Get book ID from request parameter
//             String bookIdParam = req.getParameter("id");

//             if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
//                 log.warning("Book ID parameter is missing or empty");
//                 resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
//                 return;
//             }

//             // Parse book ID
//             long bookId;
//             try {
//                 bookId = Long.parseLong(bookIdParam.trim());
//             } catch (NumberFormatException e) {
//                 log.log(Level.WARNING, "Invalid book ID format: " + bookIdParam, e);
//                 resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format");
//                 return;
//             }

//             // Fetch book from database using static method
//             Book book;
//             BookReview bookReview;
//             User sessionUser = (User) req.getSession().getAttribute("user");
//             Long currentUserId = 0L;

//             if (sessionUser != null) {
//                 currentUserId = sessionUser.getId();
//             }
//             try {
//                 book = BookDao.getBookById(bookId);
//                 bookReview = BookReviewService.getReviewsByBookId(bookId, currentUserId);
//             } catch (SQLException e) {
//                 log.log(Level.SEVERE, "Database error while fetching book with ID: " + bookId, e);
//                 resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
//                 return;
//             }

//             if (book == null) {
//                 log.warning("Book not found with ID: " + bookId);
//                 resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
//                 return;
//             }
            
//             model.User user = (model.User) req.getSession().getAttribute("user");
//                 if (user != null) {
//                 dao.ViewHistoryDao ViewHistoryDao = new dao.ViewHistoryDao();
//                 ViewHistoryDao.addHistory(user.getId(), bookId);
//             } 

//             // Set book and book-reviews as request attribute for JSP
//             req.setAttribute("book", book);
//             req.setAttribute("bookReview", bookReview);

//             // Calculate additional display information
//             boolean hasDiscount = book.getDiscount_rate() > 0;
//             req.setAttribute("hasDiscount", hasDiscount);

//             boolean inStock = book.getStock() > 0;
//             req.setAttribute("inStock", inStock);

//             // Set stock status message
//             String stockStatus;
//             if (book.getStock() == 0) {
//                 stockStatus = "Out of Stock";
//             } else if (book.getStock() <= 5) {
//                 stockStatus = "Low Stock (" + book.getStock() + " remaining)";
//             } else {
//                 stockStatus = "In Stock (" + book.getStock() + " available)";
//             }
//             req.setAttribute("stockStatus", stockStatus);

//             // Calculate savings if there's a discount
//             if (hasDiscount && book.getOriginalPrice() > 0 && book.getPrice() > 0) {
//                 double savings = book.getOriginalPrice() - book.getPrice();
//                 req.setAttribute("savings", savings);
//             }

//             // Set page metadata
//             req.setAttribute("pageTitle", book.getTitle() + " - Book Details");
//             req.setAttribute("pageDescription",
//                     "Detailed information about " + book.getTitle() + " by " + book.getAuthor());

//             log.info("Successfully loaded book details for book ID: " + bookId + " ("
//                     + book.getTitle() + ")");

//             // Forward to book detail JSP
//             req.setAttribute("contentPage", PathConstants.BOOK_DETAIL_PAGE);
//             req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);

//         } catch (Exception e) {
//             log.log(Level.SEVERE, "Error processing book detail request", e);
//             resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                     "An error occurred while loading book details");
//         }
//     }

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         // For future implementation of actions like adding to cart, wishlist, etc.
//         // For now, redirect to GET
//         doGet(req, resp);
//     }
// }
