// Package: util (ValidatorUtil)
// Các sửa đổi chính: Mở rộng regex cho phone để hỗ trợ định dạng quốc tế, thêm phương thức isValidEmail.
package util;

public class ValidatorUtil {
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\+?\\d{10,15}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}