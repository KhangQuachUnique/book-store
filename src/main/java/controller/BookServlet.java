package controller;

import dao.BookDao;
import model.Book;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

public class BookServlet extends HttpServlet {
    private BookDao dao = new BookDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Book> books = dao.getAllBooks();
        req.setAttribute("books", books);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Sample JSON response
        String json = books.get(0).toString();

        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
