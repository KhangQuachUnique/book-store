package util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

public class JsonUtil {
    private static final Gson gson = new Gson();

    // Đọc raw body JSON từ request
    public static String getJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    // Parse JSON body thành object (generic)
    public static <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        String json = getJsonBody(request);
        return gson.fromJson(json, clazz);
    }
}
