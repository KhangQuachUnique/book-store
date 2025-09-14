package util;

public class ValidatorUtil {
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^0\\d{9}$");
    }
}
