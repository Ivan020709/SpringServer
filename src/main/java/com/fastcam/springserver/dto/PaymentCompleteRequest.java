package com.fastcam.springserver.dto;
import lombok.Data;
@Data
public class PaymentCompleteRequest {
    private int userId;
    private String merchantUid;
    private String paymentUid;
}
