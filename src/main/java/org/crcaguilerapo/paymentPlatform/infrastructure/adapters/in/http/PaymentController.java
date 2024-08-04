package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.in.http;


import org.crcaguilerapo.paymentPlatform.domain.dtos.Payment;
import org.crcaguilerapo.paymentPlatform.domain.dtos.UpdateOrderStatus;
import org.crcaguilerapo.paymentPlatform.domain.exceptions.UseCaseException;
import org.crcaguilerapo.paymentPlatform.domain.usecases.PaymentUsecase;
import org.crcaguilerapo.paymentPlatform.infrastructure.services.Serialization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/v1/payments", produces = "application/json")
public class PaymentController {

    private final Serialization serialization;
    private final PaymentUsecase paymentUsecase;

    public PaymentController(Serialization serialization, PaymentUsecase paymentUsecase) {
        this.serialization = serialization;
        this.paymentUsecase = paymentUsecase;
    }


    @PostMapping
    public ResponseEntity<String> pay(
            @RequestBody Payment payment
    ) {
        try {
            paymentUsecase.pay(payment);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("");
        } catch (UseCaseException e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(serialization.objectToJson(e.errorCode));
        }
    }

    @GetMapping("/{checkoutId}")
    public ResponseEntity<String> getPayment(
            @PathVariable String checkoutId
    ) {
        var response = paymentUsecase.getPayment(checkoutId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serialization.objectToJson(response));

    }

    @PutMapping
    public ResponseEntity<String> updateOrderStatus(
            @RequestBody UpdateOrderStatus updateOrderStatus
    ) {
        paymentUsecase.updateOrderStatus(updateOrderStatus);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }
}
