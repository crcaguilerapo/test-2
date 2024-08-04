package org.crcaguilerapo.paymentPlatform.domain.dtos;

public enum PaymentOrderStatus  {
    NOT_STARTED("NOT_STARTED"),
    EXECUTING("EXECUTING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),;

    private final String status;

    PaymentOrderStatus(String status) {
        this.status = status;
    }
    public static PaymentOrderStatus fromString(String status) {
        for (PaymentOrderStatus s : PaymentOrderStatus.values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }


    public String getStatus() {
        return status;
    }
}