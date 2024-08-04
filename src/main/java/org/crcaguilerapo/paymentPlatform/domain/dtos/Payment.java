package org.crcaguilerapo.paymentPlatform.domain.dtos;

import java.util.List;

public record Payment (
        String checkoutId,
        String buyerInfo,
        CreditCardInfo creditCardInfo,
        boolean isPaymentDone,

        List<PaymentOrder> paymentOrders
) {

}
