package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.http;

import org.crcaguilerapo.paymentPlatform.domain.dtos.CreditCardInfo;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.http.dtos.Report;
import org.crcaguilerapo.paymentPlatform.infrastructure.services.Serialization;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AntiFraudController {
    private final RestTemplate restTemplate;
    private final String url;

    private final Serialization serialization;

    public AntiFraudController(
            RestTemplate restTemplate,
            Serialization serialization,
            String url
    ) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.serialization = serialization;
    }


    public boolean validateTransaction(CreditCardInfo creditCardInfo) {
        String requestBody = serialization.objectToJson(creditCardInfo);
        HttpEntity<String> request = new HttpEntity<>(requestBody);
        ResponseEntity<Report> response = restTemplate.exchange(url, HttpMethod.POST, request, Report.class);
        return response.getBody().isFraudulent();
    }
}
