package com.fastcam.springserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fastcam.springserver.dto.PaymentCompleteRequest;
import com.fastcam.springserver.dto.PaymentReadyRequest;
import com.fastcam.springserver.entity.*;
import com.fastcam.springserver.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository payments;
    private final PaymentItemRepository paymentItems;
    private final MemberItemRepository memberItems;
    private final ItemRepository items;
    private final MemberRepository members;
    private final WebClient portone = WebClient.builder().baseUrl("https://api.iamport.kr").build();

    @Value("${portone.api-key:}")
    private String apiKey;
    @Value("${portone.api-secret:}")
    private String apiSecret;

    public PaymentService(PaymentRepository payments, PaymentItemRepository paymentItems,
                          MemberItemRepository memberItems, ItemRepository items,
                          MemberRepository members) {
        this.payments = payments;
        this.paymentItems = paymentItems;
        this.memberItems = memberItems;
        this.items = items;
        this.members = members;
    }

    /** 프론트 금액을 믿지 않고 DB 가격으로 주문 금액을 계산합니다. */
    public Map<String, Object> ready(PaymentReadyRequest request) {
        requireMember(request.getUserId());
        if (request.getQuantity() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "구매 수량은 1개 이상이어야 합니다.");
        }
        Item item = items.findById(request.getItemId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."));
        if (!"Y".equals(item.getSaleYn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매 중인 아이템이 아닙니다.");
        }

        String orderNumber = "ORDER_" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_" + request.getUserId();
        int totalPrice = Math.multiplyExact(item.getPrice(), request.getQuantity());

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setMerchantUid(orderNumber);
        payment.setTotalPrice(totalPrice);
        payment.setPaymentStatus("READY");
        payment = payments.save(payment);

        PaymentItem detail = new PaymentItem();
        detail.setPaymentId(payment.getPaymentId());
        detail.setItemId(item.getItemId());
        detail.setQuantity(request.getQuantity());
        detail.setItemPrice(item.getPrice());
        detail.setSubtotal(totalPrice);
        paymentItems.save(detail);

        Map<String, Object> result = new HashMap<>();
        result.put("merchantUid", orderNumber);
        result.put("itemName", item.getItemName());
        result.put("quantity", request.getQuantity());
        result.put("totalPrice", totalPrice);
        result.put("buyerEmail", members.findByUserid(request.getUserId()).getEmail());
        return result;
    }

    /** 포트원 서버에서 실제 결제 상태와 금액을 확인한 뒤 아이템을 지급합니다. */
    public Map<String, Object> complete(PaymentCompleteRequest request) {
        Payment payment = payments.findByMerchantUid(request.getMerchantUid()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));
        if (payment.getUserId() != request.getUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 결제만 처리할 수 있습니다.");
        }
        // 결제 콜백이 중복 호출되어도 아이템을 두 번 지급하지 않습니다.
        if ("PAID".equals(payment.getPaymentStatus())) return result(payment, "ALREADY_PAID");

        JsonNode paid = getPortonePayment(request.getPaymentUid());
        String merchantUid = paid.path("merchant_uid").asText();
        String status = paid.path("status").asText();
        int amount = paid.path("amount").asInt(-1);
        if (!request.getMerchantUid().equals(merchantUid)
                || !"paid".equalsIgnoreCase(status)
                || amount != payment.getTotalPrice()) {
            payment.setPaymentStatus("FAILED");
            payments.save(payment);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 정보 또는 결제 금액이 일치하지 않습니다.");
        }

        payment.setPaymentUid(request.getPaymentUid());
        payment.setPaymentMethod(paid.path("pay_method").asText("unknown"));
        payment.setPaymentStatus("PAID");
        payment.setPaidAt(new Timestamp(System.currentTimeMillis()));
        payments.save(payment);

        for (PaymentItem detail : paymentItems.findAllByPaymentId(payment.getPaymentId())) {
            MemberItem owned = memberItems.findByUserIdAndItemId(payment.getUserId(), detail.getItemId())
                    .orElseGet(() -> {
                        MemberItem newItem = new MemberItem();
                        newItem.setUserId(payment.getUserId());
                        newItem.setItemId(detail.getItemId());
                        newItem.setQuantity(0);
                        return newItem;
                    });
            owned.setQuantity(owned.getQuantity() + detail.getQuantity());
            memberItems.save(owned);
        }
        return result(payment, "OK");
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> myList(int userId) {
        requireMember(userId);
        return payments.findAllByUserIdOrderByCreatedAtDesc(userId).stream().map(payment -> {
            Map<String, Object> row = result(payment, "OK");
            List<Map<String, Object>> details = paymentItems.findAllByPaymentId(payment.getPaymentId()).stream()
                    .map(detail -> {
                        Item item = items.findById(detail.getItemId()).orElse(null);
                        Map<String, Object> d = new HashMap<>();
                        d.put("itemName", item == null ? "삭제된 아이템" : item.getItemName());
                        d.put("quantity", detail.getQuantity());
                        d.put("itemPrice", detail.getItemPrice());
                        d.put("subtotal", detail.getSubtotal());
                        return d;
                    }).toList();
            row.put("items", details);
            return row;
        }).toList();
    }

    private JsonNode getPortonePayment(String paymentUid) {
        if (apiKey.isBlank() || apiSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "application.yaml에 포트원 API Key와 Secret을 설정해주세요.");
        }
        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("imp_key", apiKey);
        tokenBody.add("imp_secret", apiSecret);
        JsonNode tokenResponse = portone.post().uri("/users/getToken")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(tokenBody)).retrieve()
                .bodyToMono(JsonNode.class).block();
        String token = tokenResponse == null ? "" : tokenResponse.path("response").path("access_token").asText();
        if (token.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "포트원 인증에 실패했습니다.");

        JsonNode paymentResponse = portone.get().uri("/payments/{uid}", paymentUid)
                .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                .bodyToMono(JsonNode.class).block();
        JsonNode data = paymentResponse == null ? null : paymentResponse.path("response");
        if (data == null || data.isMissingNode() || data.isNull()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "포트원 결제 조회에 실패했습니다.");
        }
        return data;
    }

    private Map<String, Object> result(Payment p, String msg) {
        Map<String, Object> data = new HashMap<>();
        data.put("msg", msg);
        data.put("paymentId", p.getPaymentId());
        data.put("merchantUid", p.getMerchantUid());
        data.put("paymentUid", p.getPaymentUid() == null ? "" : p.getPaymentUid());
        data.put("totalPrice", p.getTotalPrice());
        data.put("paymentMethod", p.getPaymentMethod() == null ? "" : p.getPaymentMethod());
        data.put("paymentStatus", p.getPaymentStatus());
        data.put("paidAt", p.getPaidAt());
        data.put("createdAt", p.getCreatedAt());
        return data;
    }

    private void requireMember(int userId) {
        if (members.findByUserid(userId) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
    }
}
