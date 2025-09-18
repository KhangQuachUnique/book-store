package util;

import java.util.regex.Pattern;

/**
 * Validation utility class for common input validation operations. Provides static methods for
 * validating emails, phone numbers, and other common data formats used in the BookieCake
 * application.
 *
 * @author BookieCake Team
 * @version 1.0
 */
public class ValidatorUtil {

    // Compiled regex patterns for better performance
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{8,14}$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,20}$");

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]{2,50}$");

    /**
     * Validates an email address format.
     *
     * @param email the email address to validate
     * @return true if the email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a phone number format. Supports international formats with optional country code.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number format is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    /**
     * Validates a username format. Username must be 3-20 characters and contain only letters,
     * numbers, dots, underscores, and hyphens.
     *
     * @param username the username to validate
     * @return true if the username format is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates a person's name format. Name must be 2-50 characters and contain only letters and
     * spaces (including Vietnamese characters).
     *
     * @param name the name to validate
     * @return true if the name format is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates if a string is not null or empty.
     *
     * @param value the string to validate
     * @return true if the string is not null and not empty (after trimming), false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a string has a minimum length.
     *
     * @param value the string to validate
     * @param minLength the minimum required length
     * @return true if the string meets the minimum length requirement, false otherwise
     */
    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.trim().length() >= minLength;
    }

    /**
     * Validates if a string has a maximum length.
     *
     * @param value the string to validate
     * @param maxLength the maximum allowed length
     * @return true if the string is within the maximum length limit, false otherwise
     */
    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.trim().length() <= maxLength;
    }

    /**
     * Validates if a string length is within a specified range.
     *
     * @param value the string to validate
     * @param minLength the minimum required length
     * @param maxLength the maximum allowed length
     * @return true if the string length is within the specified range, false otherwise
     */
    public static boolean isLengthInRange(String value, int minLength, int maxLength) {
        return hasMinLength(value, minLength) && hasMaxLength(value, maxLength);
    }

    /**
     * Validates if a number is positive.
     *
     * @param number the number to validate
     * @return true if the number is greater than zero, false otherwise
     */
    public static boolean isPositiveNumber(Number number) {
        return number != null && number.doubleValue() > 0;
    }

    /**
     * Validates if a number is non-negative (zero or positive).
     *
     * @param number the number to validate
     * @return true if the number is greater than or equal to zero, false otherwise
     */
    public static boolean isNonNegativeNumber(Number number) {
        return number != null && number.doubleValue() >= 0;
    }

    /**
     * Validates if an integer is within a specified range.
     *
     * @param value the integer to validate
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return true if the value is within the specified range, false otherwise
     */
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * Validates a Vietnamese phone number format specifically. Accepts formats like: 0123456789,
     * +84123456789, 84123456789
     *
     * @param phoneNumber the Vietnamese phone number to validate
     * @return true if the phone number is a valid Vietnamese format, false otherwise
     */
    public static boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            return false;

        String cleanPhone = phoneNumber.trim().replaceAll("\\s+", "");

        // Vietnamese phone number patterns
        return cleanPhone.matches("^(0|\\+84|84)[3-9]\\d{8}$");
    }

    /**
     * Validates an age value.
     *
     * @param age the age to validate
     * @return true if the age is between 1 and 150, false otherwise
     */
    public static boolean isValidAge(Integer age) {
        return isInRange(age, 1, 150);
    }

    /**
     * Validates if a string contains only numeric characters.
     *
     * @param value the string to validate
     * @return true if the string contains only digits, false otherwise
     */
    public static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }

    /**
     * Validates if a string is a valid decimal number.
     *
     * @param value the string to validate
     * @return true if the string is a valid decimal number, false otherwise
     */
    public static boolean isValidDecimal(String value) {
        if (value == null)
            return false;

        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
