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
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    private static DecodedJWT decode(String token) {
        return VERIFIER.verify(token);
    }

    private static String generateToken(String email, String type, long expMillis, Map<String, String> claims) {
        var builder = JWT.create()
                .withSubject(email)
                .withClaim("type", type)
                .withExpiresAt(new Date(System.currentTimeMillis() + expMillis));

        if (claims != null)
            claims.forEach(builder::withClaim);
        return builder.sign(ALGORITHM);
    }

    public static String generateAccessToken(String email, String role) {
        return generateToken(email, "access", ACCESS_TOKEN_EXP, Map.of("role", role));
    }

    public static String generateRefreshToken(String email, String role) {
        return generateToken(email, "refresh", REFRESH_TOKEN_EXP, Map.of("role", role));
    }

    public static boolean validateToken(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmail(String token) {
        return decode(token).getSubject();
    }

    public static String getRole(String token) {
        return decode(token).getClaim("role").asString();
    }

    public static boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(decode(token).getClaim("type").asString());
        } catch (Exception e) {
            return false;
        }
    }
}
