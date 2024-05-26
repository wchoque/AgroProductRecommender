package com.upc.appcentroidiomas;

public enum OrderStatus {
    NEW(0, "Nueva orden"),
    PAYMENT_COMPLETED(1, "Pago completado"),
    CANCELLED(2, "Anulada"),
    RECEIVED(3, "Recibida");

    private int value;
    private String displayName;

    OrderStatus(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static OrderStatus fromInt(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus value: " + value);
    }
}
