package org.crcaguilerapo.paymentPlatform.domain.usecases;

import org.crcaguilerapo.paymentPlatform.domain.dtos.Payment;
import org.crcaguilerapo.paymentPlatform.domain.dtos.PaymentWithOutCardInfo;
import org.crcaguilerapo.paymentPlatform.domain.dtos.UpdateOrderStatus;
import org.crcaguilerapo.paymentPlatform.domain.exceptions.ErrorCode;
import org.crcaguilerapo.paymentPlatform.domain.exceptions.UseCaseException;
import org.crcaguilerapo.paymentPlatform.domain.ports.out.IPaymentRepository;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue.BankController;

public class PaymentUsecase {

    private final BankController bankController;
    private final AntiFraudUsecase antiFraudUsecase;

    private final IPaymentRepository paymentRepository;

    public PaymentUsecase(
            AntiFraudUsecase antiFraudUsecase,
            BankController bankController,
            IPaymentRepository paymentRepository
    ) {
        this.antiFraudUsecase = antiFraudUsecase;
        this.bankController = bankController;
        this.paymentRepository = paymentRepository;
    }

    public void pay(Payment payment) throws UseCaseException {
        if (antiFraudUsecase.getReport(payment.creditCardInfo())) {
            throw new UseCaseException(ErrorCode.REPORTED_FOR_FRAUD);
        }

        paymentRepository.save(payment);

        payment
                .paymentOrders()
                .forEach(bankController::send);

    }

    public PaymentWithOutCardInfo getPayment(String checkoutId) {
        return paymentRepository.getPayment(checkoutId);
    }


    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        paymentRepository.update(updateOrderStatus);
    }

}
