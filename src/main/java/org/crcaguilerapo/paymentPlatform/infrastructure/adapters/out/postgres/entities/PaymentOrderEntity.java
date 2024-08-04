package org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.entities;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class PaymentOrderEntity {
    public static Table<Record> table = table("PAYMENT_ORDER");
    public static Field<String> paymentOrderId = field("order_order_id", SQLDataType.VARCHAR(30));
    public static Field<String> sellerAccount = field("order_seller_account", SQLDataType.VARCHAR(30));
    public static Field<String> amount = field("order_amount", SQLDataType.VARCHAR(30));
    public static Field<String> currency = field("order_currency", SQLDataType.VARCHAR(30));
    public static Field<String> fkCheckoutId = field("order_fk_checkout_id", SQLDataType.VARCHAR(30));
    public static Field<String> paymentOrderStatus = field("order_payment_order_status", SQLDataType.VARCHAR(30));

}
