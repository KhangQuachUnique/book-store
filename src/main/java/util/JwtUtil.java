package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "super-secret-key"; // TODO: để env
    private static final long ACCESS_TOKEN_EXP = 15 * 60 * 1000; // 15 phút
    private static final long REFRESH_TOKEN_EXP = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    // 🔹 Sinh access token
    public static String generateToken(String email, int minutes) {
        return JWT.create()
                .withSubject(email)
                .withClaim("type", "access")
                .withExpiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .sign(algorithm);
    }

    // 🔹 Sinh access token mặc định 15 phút
    public static String generateAccessToken(String email) {
        return generateToken(email, 15);
    }

    // 🔹 Sinh refresh token
    public static String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withClaim("type", "refresh")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP))
                .sign(algorithm);
    }

    // 🔹 Validate token (check signature + expired)
    public static boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token); // sẽ throw nếu sai
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 Lấy email từ token
    public static String getEmailFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    // 🔹 Kiểm tra token có phải refresh token không
    public static boolean isRefreshToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            String type = jwt.getClaim("type").asString();
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }
}
