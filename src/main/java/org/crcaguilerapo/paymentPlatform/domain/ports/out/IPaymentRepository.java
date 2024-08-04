package org.crcaguilerapo.paymentPlatform.domain.ports.out;

import org.crcaguilerapo.paymentPlatform.domain.dtos.Payment;
import org.crcaguilerapo.paymentPlatform.domain.dtos.PaymentWithOutCardInfo;
import org.crcaguilerapo.paymentPlatform.domain.dtos.UpdateOrderStatus;

public interface IPaymentRepository {
    void save(Payment payment);
    void update(UpdateOrderStatus updateOrderStatus);
    PaymentWithOutCardInfo getPayment(String checkoutId);

}
