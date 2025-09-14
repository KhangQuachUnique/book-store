package controller;

import constant.PathConstants;
import model.Book;
import service.BookService;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        Book book = BookService.getBookById(313607);
//        double rating = book.getRating(); // vd 4.2
//        int fullStars = (int) rating;             // 4
//        double partialFraction = rating - fullStars; // 0.2
//        int emptyStars = 5 - fullStars - (partialFraction > 0 ? 1 : 0);
//
//        request.setAttribute("fullStars", fullStars);
//        request.setAttribute("partialFraction", partialFraction); // 0 if no partial
//        request.setAttribute("emptyStars", emptyStars);

        request.setAttribute("contentPage", PathConstants.VIEW_HOME);
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}

