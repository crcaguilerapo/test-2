package org.crcaguilerapo.paymentPlatform.domain.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.crcaguilerapo.paymentPlatform.domain.dtos.Payment;
import org.crcaguilerapo.paymentPlatform.domain.dtos.PaymentWithOutCardInfo;
import org.crcaguilerapo.paymentPlatform.domain.dtos.UpdateOrderStatus;
import org.crcaguilerapo.paymentPlatform.domain.exceptions.ErrorCode;
import org.crcaguilerapo.paymentPlatform.domain.exceptions.UseCaseException;
import org.crcaguilerapo.paymentPlatform.domain.ports.out.IPaymentRepository;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue.BankController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class PaymentUsecaseTest {

    @Mock
    private BankController bankController;

    @Mock
    private AntiFraudUsecase antiFraudUsecase;

    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private PaymentUsecase paymentUsecase;


    @Test
    void testPayFraudulentPaymentShouldThrowException() {
        Payment payment = mock(Payment.class);
        when(antiFraudUsecase.getReport(payment.creditCardInfo())).thenReturn(true);

        UseCaseException thrown = assertThrows(UseCaseException.class, () -> {
            paymentUsecase.pay(payment);
        });

        assertEquals(ErrorCode.REPORTED_FOR_FRAUD, thrown.errorCode);
        verify(paymentRepository, never()).save(payment);
        verify(bankController, never()).send(any());
    }

    @Test
    void testPayValidPaymentShouldSavePaymentAndSendOrders() throws UseCaseException {
        Payment payment = mock(Payment.class);
        when(antiFraudUsecase.getReport(payment.creditCardInfo())).thenReturn(false);

        paymentUsecase.pay(payment);

        verify(paymentRepository).save(payment);
        verify(bankController, times(payment.paymentOrders().size())).send(any());
    }

    @Test
    void testGetPaymentShouldReturnPayment() {
        String checkoutId = "123456";
        PaymentWithOutCardInfo expectedPayment = mock(PaymentWithOutCardInfo.class);
        when(paymentRepository.getPayment(checkoutId)).thenReturn(expectedPayment);

        PaymentWithOutCardInfo actualPayment = paymentUsecase.getPayment(checkoutId);

        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void testUpdateOrderStatusShouldCallUpdateOnRepository() {
        UpdateOrderStatus updateOrderStatus = mock(UpdateOrderStatus.class);

        paymentUsecase.updateOrderStatus(updateOrderStatus);

        verify(paymentRepository).update(updateOrderStatus);
    }
}
