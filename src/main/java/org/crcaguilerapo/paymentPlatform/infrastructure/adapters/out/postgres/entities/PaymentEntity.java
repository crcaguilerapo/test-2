package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class PaymentEntity {

    public static Table<Record> table = table("PAYMENT");
    public static Field<String> checkoutId = field("payment_checkout_id", SQLDataType.VARCHAR(30));
    public static Field<String> buyerInfo = field("payment_buyer_info", SQLDataType.VARCHAR(30));
    public static Field<String> creditCardInfo = field("payment_credit_card_info", SQLDataType.VARCHAR(30));
    public static Field<Boolean> isPaymentDone = field("payment_is_payment_done", SQLDataType.BOOLEAN);

}
