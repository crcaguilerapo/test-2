package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres;

import org.crcaguilerapo.paymentPlatform.domain.dtos.*;
import org.crcaguilerapo.paymentPlatform.domain.ports.out.IPaymentRepository;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities.PaymentEntity;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities.PaymentOrderEntity;
import org.jooq.DSLContext;

public class PaymentRepository implements IPaymentRepository {


    private final DSLContext ctx;

    public PaymentRepository(DSLContext ctx) {
        this.ctx = ctx;
    }


    @Override
    public void save(Payment payment) {
        ctx.transaction(configuration -> {
            ctx
                    .insertInto(
                            PaymentEntity.table,
                            PaymentEntity.checkoutId,
                            PaymentEntity.buyerInfo,
                            PaymentEntity.creditCardInfo,
                            PaymentEntity.isPaymentDone
                    )
                    .values(payment.checkoutId(), payment.buyerInfo(), "", false)
                    .execute();

                    payment.paymentOrders().forEach(v -> {
                        ctx.insertInto(
                                PaymentOrderEntity.table,
                                PaymentOrderEntity.paymentOrderId,
                                PaymentOrderEntity.sellerAccount,
                                PaymentOrderEntity.amount,
                                PaymentOrderEntity.currency,
                                PaymentOrderEntity.paymentOrderStatus,
                                PaymentOrderEntity.fkCheckoutId
                        )
                        .values(
                                v.paymentOrderId(),
                                v.sellerAccount(),
                                v.amount(),
                                v.currency(),
                                v.paymentOrderStatus().getStatus(),
                                payment.checkoutId()
                        )
                        .execute();
                    });
        });
    }

    @Override
    public void update(UpdateOrderStatus updateOrderStatus) {
        ctx.update(PaymentOrderEntity.table)
                .set(PaymentOrderEntity.paymentOrderStatus, updateOrderStatus.paymentOrderStatus())
                .where(PaymentOrderEntity.paymentOrderId.eq(updateOrderStatus.paymentOrderId()))
                .execute();
    }

    @Override
    public PaymentWithOutCardInfo getPayment(String checkoutId) {
        var paymentEntity = ctx.select(
                        PaymentEntity.table,
                        PaymentEntity.checkoutId,
                        PaymentEntity.buyerInfo,
                        PaymentEntity.creditCardInfo,
                        PaymentEntity.isPaymentDone
                )
                .from(PaymentEntity.table)
                .where(PaymentEntity.checkoutId.eq(checkoutId))
                .fetchOne();

        var paymentOrderEntityList = ctx.select(
                        PaymentOrderEntity.table,
                        PaymentOrderEntity.paymentOrderId,
                        PaymentOrderEntity.amount,
                        PaymentOrderEntity.currency,
                        PaymentOrderEntity.sellerAccount,
                        PaymentOrderEntity.paymentOrderStatus,
                        PaymentOrderEntity.fkCheckoutId
                )
                .from(PaymentOrderEntity.table)
                .where(PaymentOrderEntity.fkCheckoutId.eq(paymentEntity.get(PaymentEntity.checkoutId)))
                .fetch();

        var mapperPaymentOrder = paymentOrderEntityList
                    .map(record -> new PaymentOrder(
                        record.get(PaymentOrderEntity.paymentOrderId),
                        record.get(PaymentOrderEntity.sellerAccount),
                        record.get(PaymentOrderEntity.amount),
                        record.get(PaymentOrderEntity.currency),
                        PaymentOrderStatus.fromString(record.get(PaymentOrderEntity.paymentOrderStatus))
                    ));

        return paymentEntity
                .map(record -> new PaymentWithOutCardInfo(
                        record.get(PaymentEntity.checkoutId),
                        record.get(PaymentEntity.buyerInfo),
                        record.get(PaymentEntity.isPaymentDone),
                        mapperPaymentOrder
                ));

    }


}
