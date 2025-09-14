package controller;

import constant.PathConstants;
import model.Book;
import model.BookReview;
import service.BookReviewService;
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
//
        BookReview bookReview = BookReviewService.getReviewsByBookId(318041);

        request.setAttribute("bookReview", bookReview);

        request.setAttribute("contentPage", PathConstants.VIEW_HOME);
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}

