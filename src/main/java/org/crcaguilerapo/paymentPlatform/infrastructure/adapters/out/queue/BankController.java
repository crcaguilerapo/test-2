package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue;


import org.crcaguilerapo.paymentPlatform.domain.dtos.PaymentOrder;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue.dtos.TransactionMessage;
import org.crcaguilerapo.paymentPlatform.infrastructure.services.Serialization;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class BankController {

    private final SqsClient sqsClient;
    private final Serialization serialization;
    private final String url;

    public BankController(SqsClient sqsClient, Serialization serialization, String url) {
        this.sqsClient = sqsClient;
        this.serialization = serialization;
        this.url = url;
    }

    public void send(PaymentOrder paymentOrder) {
        var msg = paymentOrderToTransactionMessage(paymentOrder);
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(url)
                .messageBody(serialization.objectToJson(msg))
                .build();

        sqsClient.sendMessage(sendMsgRequest);
    }

    private TransactionMessage paymentOrderToTransactionMessage(PaymentOrder paymentOrder) {
        return new TransactionMessage(
                paymentOrder.sellerAccount(),
                paymentOrder.amount(),
                paymentOrder.currency(),
                paymentOrder.paymentOrderId()
        );
    }

}