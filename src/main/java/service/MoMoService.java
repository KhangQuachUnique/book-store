package service;



import com.google.gson.Gson;

import model.MoMoPaymentResponse;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;

import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;

import java.security.GeneralSecurityException;

import java.util.Base64;

import java.util.LinkedHashMap;

import java.util.Map;

public class MoMoService {

    private static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";

    private static final String PARTNER_CODE = "MOMO";

    private static final String PARTNER_NAME = "BookieCake";

    private static final String STORE_ID = "BookieCakeStore";

    private static final String ACCESS_KEY = "F8BBA842ECF85";

    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

    private static final String REQUEST_TYPE = "captureWallet";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final Gson gson = new Gson();

    /**

     * Create MoMo payment request

     * @param orderId    Order ID

     * @param amount     Amount in VND

     * @param orderInfo  Order information

     * @return MoMo payment response with payUrl

     */

    public MoMoPaymentResponse createPayment(String orderId,

                                             String requestId,

                                             long amount,

                                             String orderInfo,

                                             String redirectUrl,

                                             String ipnUrl,

                                             String extraData) throws IOException, InterruptedException, GeneralSecurityException {

        if (orderId == null || requestId == null) {

            throw new IllegalArgumentException("orderId and requestId are required");

        }



        String normalizedOrderInfo = orderInfo != null ? orderInfo : ("Thanh toán đơn hàng " + orderId);

        String normalizedRedirect = redirectUrl != null ? redirectUrl : "";

        String normalizedIpn = ipnUrl != null ? ipnUrl : "";

        String encodedExtraData = extraData != null

                ? Base64.getEncoder().encodeToString(extraData.getBytes(StandardCharsets.UTF_8))

                : "";



        Map<String, Object> payload = new LinkedHashMap<>();

        payload.put("partnerCode", PARTNER_CODE);

        payload.put("partnerName", PARTNER_NAME);

        payload.put("storeId", STORE_ID);

        payload.put("requestType", REQUEST_TYPE);

        payload.put("orderId", orderId);

        payload.put("requestId", requestId);

        payload.put("amount", String.valueOf(amount));

        payload.put("orderInfo", normalizedOrderInfo);

        payload.put("redirectUrl", normalizedRedirect);

        payload.put("ipnUrl", normalizedIpn);

        payload.put("lang", "vi");

        payload.put("autoCapture", true);

        payload.put("extraData", encodedExtraData);



        String rawSignature = String.format("accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",

                ACCESS_KEY,

                payload.get("amount"),

                encodedExtraData,

                normalizedIpn,

                orderId,

                normalizedOrderInfo,

                PARTNER_CODE,

                normalizedRedirect,

                requestId,

                REQUEST_TYPE);



        payload.put("signature", sign(rawSignature));



        String body = gson.toJson(payload);



        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(ENDPOINT))

                .header("Content-Type", "application/json")

                .POST(HttpRequest.BodyPublishers.ofString(body))

                .build();



        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {

            throw new IOException("MoMo API returned status " + response.statusCode() + ": " + response.body());

        }

        return gson.fromJson(response.body(), MoMoPaymentResponse.class);

    }



    private String sign(String rawData) throws GeneralSecurityException {

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {

            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {

                hexString.append('0');

            }

            hexString.append(hex);

        }

        return hexString.toString();

    }

}
