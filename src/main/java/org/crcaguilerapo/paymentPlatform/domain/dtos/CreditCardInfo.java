package org.crcaguilerapo.paymentPlatform.domain.dtos;

public record CreditCardInfo(
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv,
        String cardType
) {

}
