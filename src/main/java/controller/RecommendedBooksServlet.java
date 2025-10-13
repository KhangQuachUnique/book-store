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

        List<Book> recommendedBooks = new ArrayList<>();
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            WishList wishList = wishListService.getWishListBooks(user.getId());

            if (wishList != null && wishList.getItems() != null && !wishList.getItems().isEmpty()) {
                Map<Long, Integer> categoryCount = new HashMap<>();

                for (WishListItem item : wishList.getItems()) {
                    try {
                        long bookId = (long) item.getBook().getId();
                        Book book = bookService.getBookById(bookId);
                        Long categoryId = book.getCategory().getId();

                        categoryCount.put(categoryId, categoryCount.getOrDefault(categoryId, 0) + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                List<Long> topCategories = categoryCount.entrySet()
                        .stream()
                        .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                        .limit(3)
                        .map(Map.Entry::getKey)
                        .toList();

                for (Long categoryId : topCategories) {
                    List<Book> booksByCategory = bookService.getAllBooksByCategoryId(categoryId);
                    recommendedBooks.addAll(booksByCategory);
                }

                Collections.shuffle(recommendedBooks);
            }
        }

        request.setAttribute("recommendedBooks", recommendedBooks);

        List<Book> topSellingBooks = bookService.getTopSellingBooks();
        request.setAttribute("topSellingBooks", topSellingBooks);

        request.getRequestDispatcher("/WEB-INF/views/recommendedBooks.jsp").include(request, response);
    }
}
