package controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import service.BookService;
import service.WishListService;

@WebServlet("/recommend-books")
public class RecommendedBooksServlet extends HttpServlet {

    private static final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WishListService wishListService = new WishListService();
        BookService bookService = new BookService();

        List<Book> recommendedBooks = new ArrayList<>();
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            System.out.println("[DEBUG] 1");
            WishList wishList = wishListService.getWishListBooks(user.getId());
            System.out.println("[DEBUG] 2");

            if (wishList != null) {
                Map<Long, Integer> categoryCount = new HashMap<>();

                for (WishListItem item : wishList.getItems()) {
                    try {
                        Long bookId = (long) item.getBook().getId();
                        Book book = bookService.getBookById(bookId);
                        Long categoryId = book.getCategory().getId();

                        categoryCount.put(categoryId, categoryCount.getOrDefault(categoryId, 0) + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Long mostPopularCategoryId = categoryCount.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);

                if (mostPopularCategoryId != null) {
                    recommendedBooks = bookService.getAllBooksByCategoryId(mostPopularCategoryId);
                }
            }
        }

        request.setAttribute("recommendedBooks", recommendedBooks);

        // Top Selling Books
        List<Book> topSellingBooks = bookService.getTopSellingBooks();
        request.setAttribute("topSellingBooks", topSellingBooks);

        request.getRequestDispatcher("/WEB-INF/views/recommendedBooks.jsp").include(request, response);
    }

}