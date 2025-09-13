package service;

import dao.AddressDAO;
import model.Address;

import java.sql.SQLException;
import java.util.List;

public class AddressService {
    private AddressDAO addressDAO = new AddressDAO();

    public List<Address> getAddressesByUserId(long userId) throws SQLException {
        return addressDAO.getAddressesByUserId(userId);
    }

    public void createAddress(Address address) throws SQLException {
        addressDAO.createAddress(address);
        if (address.isDefaultAddress()) {
            addressDAO.setDefaultAddress(address.getId(), address.getUserId());
        }
    }

    public void setDefaultAddress(long addressId, long userId) throws SQLException {
        addressDAO.setDefaultAddress(addressId, userId);
    }
}