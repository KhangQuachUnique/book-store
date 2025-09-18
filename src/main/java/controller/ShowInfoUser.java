package controller;

import constant.PathConstants;
import model.Address;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/info")
public class ShowInfoUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        String defaultAddress = null;
        @SuppressWarnings("unchecked")
        List<Address> addressList = (List<Address>) req.getSession().getAttribute("addresses");

        if (addressList != null) {
            for (Address addr : addressList) {
                if (addr.getIsDefaultAddress() != null && addr.getIsDefaultAddress()) {
                    defaultAddress = addr.getAddress();
                    break; // tìm thấy thì thoát luôn
                }
            }
        }

        if (defaultAddress == null) {
            defaultAddress = "None";
        }

        req.getSession().setAttribute("defaultAddress", defaultAddress);

        String page = PathConstants.VIEW_USER_INFO;
        req.getSession().setAttribute("contentPage", page);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
