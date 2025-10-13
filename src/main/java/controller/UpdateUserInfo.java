package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Address;
import model.User;
import service.AddressService;
import service.UserService;

@WebServlet("/user/update")
public class UpdateUserInfo extends HttpServlet {
    private final UserService userService = new UserService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            updateInfoUser(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInfoUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        User sessionUser = (User) request.getSession().getAttribute("user");
        List<Address> addresses = sessionUser.getAddresses();
        String defaultAddress = request.getSession().getAttribute("defaultAddress").toString();

        String message = "Information has not changed!";
        String url = "/user/info";

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        boolean isChanged = false;

        if (!sessionUser.getName().equals(name) || !sessionUser.getPhoneNumber().equals(phone)) {
            isChanged = true;
            sessionUser.setName(name);
            sessionUser.setPhoneNumber(phone);
            userService.updateUser(sessionUser);
        }

        if (!defaultAddress.equals(address)) {
            isChanged = true;

            boolean exists = addresses.stream()
                    .anyMatch(a -> a.getAddress().equalsIgnoreCase(address));
            if (exists) {
                long addressId = addresses.stream()
                        .filter(a -> a.getAddress().equalsIgnoreCase(address))
                        .map(Address::getId)
                        .findFirst()
                        .orElse(0L);

                if (addressId != 0L) {
                    addressService.setDefaultAddress(addressId, sessionUser.getId());
                }
            } else {
                addressService.createAddress(sessionUser.getId() , address, false);
            }

            List<Address> updatedAddresses = addressService.getAddressesByUserId(sessionUser.getId());
            sessionUser.setAddresses(updatedAddresses);

            request.getSession().setAttribute("defaultAddress", address);
        }

        if (isChanged) {
            request.getSession().setAttribute("user", sessionUser);
            message = "Information updated successfully!";
        }

        request.getSession().setAttribute("toastMessage", message);
        response.sendRedirect(request.getContextPath() + url);
    }
}