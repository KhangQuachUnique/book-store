package controller;

import constant.PathConstants;
import dao.CategoryBookDao;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoryBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Book> books = CategoryBookDao.getAllBook();
        req.setAttribute("books", books);
        req.setAttribute("contentPage", "/WEB-INF/views/CategoryBook.jsp");
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }
}
