package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handle MoMo IPN (Instant Payment Notification)
 * This is called by MoMo server to notify payment status
 */
@WebServlet("/payment/momo/ipn")
public class MoMoIpnServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MoMoIpnServlet.class.getName());
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        }
        
        String payloadStr = payload.toString();
        log.info("Received MoMo IPN payload: " + payloadStr);
        
        try {
            // Parse JSON payload
            JsonObject json = gson.fromJson(payloadStr, JsonObject.class);
            
            if (json != null && json.has("resultCode")) {
                int resultCode = json.get("resultCode").getAsInt();
                String orderId = json.has("orderId") ? json.get("orderId").getAsString() : "unknown";
                String message = json.has("message") ? json.get("message").getAsString() : "";
                
                if (resultCode == 0) {
                    log.info("MoMo IPN: Payment successful for order " + orderId);
                    // Payment successful - order should be created in callback servlet
                } else {
                    log.warning("MoMo IPN: Payment failed for order " + orderId + " - " + message);
                    // Payment failed
                }
            }
            
            // Always respond with 204 No Content to acknowledge receipt
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error processing MoMo IPN", e);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // Still acknowledge receipt
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.log(Level.INFO, "MoMo IPN endpoint accessed via GET");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}