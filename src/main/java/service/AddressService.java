//package service;
//
//import model.Address;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class AddressService {
//    private AddressDao addressDao = new AddressDao();
//
//    public List<Address> getAddressesByUserId(long userId) throws SQLException {
//        return addressDao.getAddressesByUserId(userId);
//    }
//
//    public void createAddress(long userId, String addressText, boolean isDefaultAddress) throws SQLException {
//        long newAddressId = addressDao.createAddress(userId, addressText, isDefaultAddress);
//        if (isDefaultAddress) {
//            addressDao.setDefaultAddress(newAddressId, userId);
//        }
//    }
//
//    public void updateAddress(long userId, long addressId, String addressText, boolean isDefaultAddress) throws SQLException {
//        addressDao.updateAddress(userId, addressId, addressText, isDefaultAddress);
//    }
//
//    public void deleteAddress(long addressId, long userId) throws SQLException {
//        Address address = addressDao.findByIdAndUserId(addressId, userId);
//        if (address == null) {
//            throw new SQLException("Address not found");
//        }
//        if (address.isDefaultAddress()) { // Changed from isDefault() to isDefaultAddress()
//            throw new SQLException("Cannot delete default address");
//        }
//        addressDao.deleteAddress(addressId, userId);
//    }
//
//    public void setDefaultAddress(long addressId, long userId) throws SQLException {
//        addressDao.setDefaultAddress(addressId, userId);
//    }
//}