package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoMoPaymentResponse {
    private Integer resultCode;
    private String message;
    private String orderId;
    private String requestId;
    private String payUrl;
    private String deeplink;
    private String deeplinkWeb;
    private String qrCodeUrl;
    private String signature;
}
