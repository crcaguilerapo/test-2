package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres;

import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities.PaymentEntity;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities.PaymentOrderEntity;
import org.jooq.DSLContext;

public class Seeders {
    private final DSLContext ctx;

    public Seeders(DSLContext ctx) {
        this.ctx = ctx;
    }


    public void start() {
        ctx.dropTableIfExists(PaymentEntity.table).execute();
        ctx.dropTableIfExists(PaymentOrderEntity.table).execute();

        ctx
                .createTable(PaymentEntity.table)
                .column(PaymentEntity.checkoutId)
                .column(PaymentEntity.creditCardInfo)
                .column(PaymentEntity.buyerInfo)
                .column(PaymentEntity.isPaymentDone)
                .execute();

        ctx
                .createTable(PaymentOrderEntity.table)
                .column(PaymentOrderEntity.paymentOrderId)
                .column(PaymentOrderEntity.sellerAccount)
                .column(PaymentOrderEntity.amount)
                .column(PaymentOrderEntity.currency)
                .column(PaymentOrderEntity.paymentOrderStatus)
                .column(PaymentOrderEntity.fkCheckoutId)
                .execute();

    }
}