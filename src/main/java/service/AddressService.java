package service;

import java.sql.SQLException;
import java.util.List;
import dao.AddressDao;
import model.Address;

public class AddressService {
    private AddressDao addressDAO = new AddressDao();

    public List<Address> getAddressesByUserId(long userId) throws SQLException {
        return addressDAO.getAddressesByUserId(userId);
    }

    public void createAddress(Address address) throws SQLException {
        addressDAO.createAddress(address);
        if (address.getIsDefaultAddress()) {
            addressDAO.setDefaultAddress(address.getId(), address.getUserId());
        }
    }

    public void deleteAddress(long addressId, long userId) throws SQLException {
        Address address = getAddressesByUserId(userId).stream().filter(a -> a.getId() == addressId)
                .findFirst().orElseThrow(() -> new SQLException("Address not found"));
        if (address.getIsDefaultAddress()) {
            throw new SQLException("Cannot delete default address");
        }
        addressDAO.deleteAddress(addressId, userId);
    }

    public void setDefaultAddress(long addressId, long userId) throws SQLException {
        addressDAO.setDefaultAddress(addressId, userId);
    }
}
