package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;
import model.Book;
import model.User;
import service.BookService;

@WebServlet("/recommend-books")
public class RecommendedBooks extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Long> recommendedBookIds = Arrays.asList(
                313607L, 315886L, 315931L, 317128L, 318041L, 330658L, 330880L, 357782L,
                385504L, 403793L, 412218L, 418263L, 421281L, 430252L, 435269L, 437691L,
                437744L, 442343L, 466745L, 508898L, 513007L, 518947L, 526514L, 546201L
        );
        List<Book> recommendedBooks = null;

        try {
            recommendedBooks = BookService.getBooksByIds(recommendedBookIds);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("recommendedBooks", recommendedBooks);

        request.getRequestDispatcher("/WEB-INF/views/recommendedBooks.jsp").include(request, response);
    }
}
