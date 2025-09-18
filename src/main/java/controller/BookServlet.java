package controller;

import com.google.gson.Gson;
import model.Book;
import service.BookService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/book")
public class BookServlet extends HttpServlet {
   private final BookService bookService = new BookService();

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       List<Book> books = bookService.getAllBooks();
       req.setAttribute("books", books);
       resp.setContentType("application/json");
       resp.setCharacterEncoding("UTF-8");

       Gson gson = new Gson();
       String json = gson.toJson(books); // convert cả list -> JSON
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
