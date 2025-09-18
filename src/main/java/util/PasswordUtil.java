// Package: util (PasswordUtil)
// Các sửa đổi chính: Thay SHA-256 bằng BCrypt để thống nhất và an toàn hơn cho việc lưu trữ mật khẩu.
package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}