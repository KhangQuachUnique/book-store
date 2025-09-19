package service;

import dao.AddressDao;
import model.Address;

import java.sql.SQLException;
import java.util.List;

public class AddressService {
    private AddressDao addressDao = new AddressDao();

    public List<Address> getAddressesByUserId(long userId) throws SQLException {
        return addressDao.getAddressesByUserId(userId);
    }

    public void createAddress(Address address) throws SQLException {
        addressDao.createAddress(address);
        if (address.isDefaultAddress()) {
            addressDao.setDefaultAddress(address.getId(), address.getUserId());
        }
    }

    // CHANGE: Thêm updateAddress để hỗ trợ inline edit từ viewUser
    public void updateAddress(Address address) throws SQLException {
        addressDao.updateAddress(address);
    }

    public void deleteAddress(long addressId, long userId) throws SQLException {
        Address address = getAddressesByUserId(userId).stream()
                .filter(a -> a.getId() == addressId)
                .findFirst()
                .orElseThrow(() -> new SQLException("Address not found"));
        if (address.isDefaultAddress()) {
            throw new SQLException("Cannot delete default address");
        }
        addressDao.deleteAddress(addressId, userId);
    }

    public void setDefaultAddress(long addressId, long userId) throws SQLException {
        addressDao.setDefaultAddress(addressId, userId);
    }
}