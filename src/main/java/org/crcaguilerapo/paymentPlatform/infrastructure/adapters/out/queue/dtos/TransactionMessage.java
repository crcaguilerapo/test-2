package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue.dtos;

public record TransactionMessage(
        String sellerAccount,
        String amount,
        String currency,
        String paymentOrderId
) {
}
