package org.crcaguilerapo.paymentPlatform.domain.dtos;

public record PaymentOrder(
    String sellerAccount,
    String amount,
    String currency,
    String paymentOrderId,
    PaymentOrderStatus paymentOrderStatus
) {

}
