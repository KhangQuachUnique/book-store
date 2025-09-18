package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.Map;

/**
 * JWT (JSON Web Token) utility class for authentication and authorization.
 * Handles token generation, validation, and claim extraction for the BookieCake application.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
public class JwtUtil {
    
    // JWT Configuration Constants
    private static final String SECRET_KEY = "super-secret-key"; // TODO: Move to environment variable
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;       // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days
    
    // JWT Algorithm and Verifier (initialized once for performance)
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();
    
    // Token type constants
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    
    /**
     * Decodes and verifies a JWT token.
     * 
     * @param token the JWT token to decode
     * @return the decoded JWT
     * @throws JWTVerificationException if the token is invalid
     */
    private static DecodedJWT decodeToken(String token) throws JWTVerificationException {
        return VERIFIER.verify(token);
    }
    
    /**
     * Generates a JWT token with specified parameters.
     * 
     * @param email the user's email (subject)
     * @param tokenType the type of token (access or refresh)
     * @param expirationTime the expiration time in milliseconds
     * @param claims additional claims to include in the token
     * @return the generated JWT token string
     */
    private static String generateToken(String email, String tokenType, long expirationTime, Map<String, String> claims) {
        var builder = JWT.create()
                .withSubject(email)
                .withClaim("type", tokenType)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime));
        
        // Add additional claims if provided
        if (claims != null) {
            claims.forEach(builder::withClaim);
        }
        
        return builder.sign(ALGORITHM);
    }
    
    /**
     * Generates an access token for user authentication.
     * 
     * @param email the user's email
     * @param role the user's role (e.g., "user", "admin")
     * @return the generated access token
     */
    public static String generateAccessToken(String email, String role) {
        return generateToken(email, ACCESS_TOKEN_TYPE, ACCESS_TOKEN_EXPIRATION, Map.of("role", role));
    }
    
    /**
     * Generates a refresh token for token renewal.
     * 
     * @param email the user's email
     * @param role the user's role (e.g., "user", "admin")
     * @return the generated refresh token
     */
    public static String generateRefreshToken(String email, String role) {
        return generateToken(email, REFRESH_TOKEN_TYPE, REFRESH_TOKEN_EXPIRATION, Map.of("role", role));
    }
    
    /**
     * Validates a JWT token.
     * 
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    public static boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        try {
            decodeToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    
    /**
     * Extracts the email (subject) from a JWT token.
     * 
     * @param token the JWT token
     * @return the email from the token
     * @throws JWTVerificationException if the token is invalid
     */
    public static String getEmail(String token) throws JWTVerificationException {
        return decodeToken(token).getSubject();
    }
    
    /**
     * Extracts the role claim from a JWT token.
     * 
     * @param token the JWT token
     * @return the role from the token, or null if not present
     * @throws JWTVerificationException if the token is invalid
     */
    public static String getRole(String token) throws JWTVerificationException {
        return decodeToken(token).getClaim("role").asString();
    }
    
    /**
     * Checks if a token is a refresh token.
     * 
     * @param token the JWT token to check
     * @return true if the token is a refresh token, false otherwise
     */
    public static boolean isRefreshToken(String token) {
        try {
            String tokenType = decodeToken(token).getClaim("type").asString();
            return REFRESH_TOKEN_TYPE.equals(tokenType);
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    
    /**
     * Checks if a token is an access token.
     * 
     * @param token the JWT token to check
     * @return true if the token is an access token, false otherwise
     */
    public static boolean isAccessToken(String token) {
        try {
            String tokenType = decodeToken(token).getClaim("type").asString();
            return ACCESS_TOKEN_TYPE.equals(tokenType);
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    
    /**
     * Gets the expiration time of a token.
     * 
     * @param token the JWT token
     * @return the expiration date, or null if the token is invalid
     */
    public static Date getExpirationTime(String token) {
        try {
            return decodeToken(token).getExpiresAt();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
