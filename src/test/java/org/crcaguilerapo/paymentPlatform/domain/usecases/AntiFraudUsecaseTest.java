package org.crcaguilerapo.paymentPlatform.domain.usecases;

import org.crcaguilerapo.paymentPlatform.domain.dtos.CreditCardInfo;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.http.AntiFraudController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AntiFraudUsecaseTest {

    @Mock
    private AntiFraudController antiFraudController;

    @InjectMocks
    private AntiFraudUsecase antiFraudUsecase;


    @Test
    void testGetReportValidTransaction() {
        CreditCardInfo creditCardInfo = new CreditCardInfo(
                "1234-5678-9012-3456",
                "John Doe",
                "12/25",
                "123",
                "Visa"
        );
        when(antiFraudController.validateTransaction(creditCardInfo)).thenReturn(true);

        boolean result = antiFraudUsecase.getReport(creditCardInfo);

        assertTrue(result, "Expected the result to be true for a valid transaction");
    }

    @Test
    void testGetReport_InvalidTransaction() {
        CreditCardInfo creditCardInfo = new CreditCardInfo(
                "1234-5678-9012-3456",
                "John Doe",
                "12/25",
                "123",
                "Visa"
        );
        when(antiFraudController.validateTransaction(creditCardInfo)).thenReturn(false);

        boolean result = antiFraudUsecase.getReport(creditCardInfo);

        assertFalse(result, "Expected the result to be false for an invalid transaction");
    }
}