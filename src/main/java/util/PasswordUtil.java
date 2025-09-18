package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility class for secure password hashing and verification.
 * Uses BCrypt algorithm for strong password security in the BookieCake application.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
public class PasswordUtil {
    
    // BCrypt work factor (rounds of hashing) - higher values are more secure but slower
    private static final int BCRYPT_ROUNDS = 12;
    
    /**
     * Hashes a plain text password using BCrypt algorithm.
     * 
     * @param plainPassword the plain text password to hash
     * @return the hashed password string
     * @throws IllegalArgumentException if the password is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verifies a plain text password against a hashed password.
     * 
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the hashed password to check against
     * @return true if the password matches, false otherwise
     * @throws IllegalArgumentException if either parameter is null or empty
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Plain password cannot be null or empty");
        }
        
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }
    
    /**
     * Validates password strength based on common security requirements.
     * 
     * @param password the password to validate
     * @return true if the password meets strength requirements, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Check for at least one lowercase letter
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        
        // Check for at least one uppercase letter
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        
        // Check for at least one digit
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        // Check for at least one special character
        boolean hasSpecialChar = password.chars().anyMatch(ch -> 
            "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar;
    }
    
    /**
     * Generates a password strength message for user feedback.
     * 
     * @param password the password to evaluate
     * @return a descriptive message about password strength
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        if (isPasswordStrong(password)) {
            return "Strong password";
        }
        
        return "Password should contain uppercase, lowercase, digit, and special character";
    }
}