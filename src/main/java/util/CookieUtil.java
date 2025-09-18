package util;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cookie utility class for managing HTTP cookies and token handling.
 * Provides convenient methods for cookie operations and JWT token management.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
public class CookieUtil {
    
    // Cookie name constants
    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    
    // Cookie settings
    private static final int ACCESS_TOKEN_MAX_AGE = 15 * 60; // 15 minutes
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; // 7 days
    
    /**
     * Gets a valid access token, refreshing it if necessary.
     * This method checks for an existing access token and attempts to refresh it
     * using the refresh token if the access token is expired or invalid.
     * 
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @return a valid access token, or null if unable to obtain one
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static String getValidAccessToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accessToken = getCookieValue(request, ACCESS_TOKEN_COOKIE);
        
        // If access token is missing or invalid, try to refresh it
        if (accessToken == null || !JwtUtil.validateToken(accessToken)) {
            // Call internal refresh servlet to get new access token
            request.getRequestDispatcher("/user/refresh").include(request, response);
            accessToken = getCookieValue(request, ACCESS_TOKEN_COOKIE);
        }
        
        // Return the token only if it's valid
        return (accessToken != null && JwtUtil.validateToken(accessToken)) ? accessToken : null;
    }
    
    /**
     * Retrieves the value of a specific cookie from the request.
     * 
     * @param request the HTTP servlet request
     * @param cookieName the name of the cookie to retrieve
     * @return the cookie value, or null if the cookie is not found
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    /**
     * Creates a secure HTTP-only cookie with appropriate settings.
     * 
     * @param name the cookie name
     * @param value the cookie value
     * @param maxAge the maximum age in seconds (0 to delete, -1 for session)
     * @param httpOnly whether the cookie should be HTTP-only
     * @return the configured Cookie object
     */
    public static Cookie createSecureCookie(String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        // Note: setSecure(true) should be enabled in production with HTTPS
        return cookie;
    }
    
    /**
     * Sets an access token cookie in the response.
     * 
     * @param response the HTTP servlet response
     * @param accessToken the access token value
     */
    public static void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = createSecureCookie(ACCESS_TOKEN_COOKIE, accessToken, ACCESS_TOKEN_MAX_AGE, true);
        response.addCookie(cookie);
    }
    
    /**
     * Sets a refresh token cookie in the response.
     * 
     * @param response the HTTP servlet response
     * @param refreshToken the refresh token value
     */
    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = createSecureCookie(REFRESH_TOKEN_COOKIE, refreshToken, REFRESH_TOKEN_MAX_AGE, true);
        response.addCookie(cookie);
    }
    
    /**
     * Removes (deletes) a cookie by setting its max age to 0.
     * 
     * @param response the HTTP servlet response
     * @param cookieName the name of the cookie to remove
     */
    public static void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = createSecureCookie(cookieName, "", 0, true);
        response.addCookie(cookie);
    }
    
    /**
     * Removes all authentication-related cookies (access and refresh tokens).
     * 
     * @param response the HTTP servlet response
     */
    public static void removeAuthCookies(HttpServletResponse response) {
        removeCookie(response, ACCESS_TOKEN_COOKIE);
        removeCookie(response, REFRESH_TOKEN_COOKIE);
    }
    
    /**
     * Checks if the user is logged in based on the presence of valid cookies.
     * 
     * @param request the HTTP servlet request
     * @return true if the user has valid authentication cookies, false otherwise
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        String accessToken = getCookieValue(request, ACCESS_TOKEN_COOKIE);
        String refreshToken = getCookieValue(request, REFRESH_TOKEN_COOKIE);
        
        // User is logged in if they have a valid access token or a valid refresh token
        return (accessToken != null && JwtUtil.validateToken(accessToken)) ||
               (refreshToken != null && JwtUtil.validateToken(refreshToken) && JwtUtil.isRefreshToken(refreshToken));
    }
}
