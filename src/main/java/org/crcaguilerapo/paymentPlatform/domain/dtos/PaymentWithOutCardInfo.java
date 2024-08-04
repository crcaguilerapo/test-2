package org.crcaguilerapo.paymentPlatform.domain.dtos;

import java.util.List;

public record PaymentWithOutCardInfo(
        String checkoutId,
        String buyerInfo,
        boolean isPaymentDone,
        List<PaymentOrder> paymentOrders
) {

}
