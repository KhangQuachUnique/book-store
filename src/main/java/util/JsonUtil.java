package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * JSON utility class for handling JSON serialization and deserialization. Provides convenient
 * methods for working with JSON data in servlet requests and responses.
 *
 * @author BookieCake Team
 * @version 1.0
 */
public class JsonUtil {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * Reads the raw JSON body from an HTTP request.
     *
     * @param request the HttpServletRequest containing JSON data
     * @return the JSON string from the request body
     * @throws IOException if an I/O error occurs while reading the request
     */
    public static String getJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonBody = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
        }

        return jsonBody.toString();
    }

    /**
     * Parses JSON from HTTP request body into a specified class type.
     *
     * @param <T> the type to deserialize to
     * @param request the HttpServletRequest containing JSON data
     * @param clazz the Class object representing the target type
     * @return an instance of the specified class populated with JSON data
     * @throws IOException if an I/O error occurs while reading the request
     * @throws JsonSyntaxException if the JSON is malformed
     */
    public static <T> T parseJson(HttpServletRequest request, Class<T> clazz)
            throws IOException, JsonSyntaxException {
        String jsonBody = getJsonBody(request);

        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Request body is empty or null");
        }

        return GSON.fromJson(jsonBody, clazz);
    }

    /**
     * Converts an object to JSON string.
     *
     * @param object the object to serialize to JSON
     * @return JSON string representation of the object
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Sends a JSON response to the client.
     *
     * @param response the HttpServletResponse to write to
     * @param object the object to serialize and send as JSON
     * @throws IOException if an I/O error occurs while writing the response
     */
    public static void sendJsonResponse(HttpServletResponse response, Object object)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.write(toJson(object));
            writer.flush();
        }
    }

    /**
     * Sends an error response in JSON format.
     *
     * @param response the HttpServletResponse to write to
     * @param statusCode the HTTP status code
     * @param message the error message
     * @throws IOException if an I/O error occurs while writing the response
     */
    public static void sendErrorResponse(HttpServletResponse response, int statusCode,
            String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorJson =
                String.format("{\"error\": \"%s\", \"status\": %d}", message, statusCode);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(errorJson);
            writer.flush();
        }
    }
}
