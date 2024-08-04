package org.crcaguilerapo.paymentPlatform.domain.usecases;

import org.crcaguilerapo.paymentPlatform.domain.dtos.CreditCardInfo;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.http.AntiFraudController;


public class AntiFraudUsecase {

    private final AntiFraudController antiFraudController;

    public AntiFraudUsecase(AntiFraudController antiFraudController) {
        this.antiFraudController = antiFraudController;
    }


    public boolean getReport(CreditCardInfo creditCardInfo) {
        return antiFraudController.validateTransaction(creditCardInfo);
    }
}
