package util;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
    private static final String SECRET = "super-secret-key";
    private static final long ACCESS_TOKEN_EXP = 15 * 60 * 1000; // 15 phút
    private static final long REFRESH_TOKEN_EXP = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    public static DecodedJWT decode(String token) {
        return verifier.verify(token); // verify và trả về object
    }

    // Hàm generate token chung
    private static String generateToken(String email, String type, long expMillis, Map<String, String> extraClaims) {
        var builder = JWT.create().withSubject(email).withClaim("type", type)
                .withExpiresAt(new Date(System.currentTimeMillis() + expMillis));

        if (extraClaims != null) {
            extraClaims.forEach(builder::withClaim);
        }

        return builder.sign(algorithm);
    }

    // Access token
    public static String generateAccessToken(String email, String role) {
        return generateToken(email, "access", ACCESS_TOKEN_EXP, Map.of("role", role));
    }

    // Refresh token
    public static String generateRefreshToken(String email, String role) {
        return generateToken(email, "refresh", REFRESH_TOKEN_EXP, Map.of("role", role));
    }

    // Validate token
    public static boolean validateToken(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Lấy email
    public static String getEmailFromToken(String token) {
        return decode(token).getSubject();
    }

    // Lấy role
    public static String getRoleFromToken(String token) {
        return decode(token).getClaim("role").asString();
    }

    // Check refresh token
    public static boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(decode(token).getClaim("type").asString());
        } catch (Exception e) {
            return false;
        }
    }
}
