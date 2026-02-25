package com.restaurant.models;

/**
 * Enumerador OrderStatus para los estados de un pedido.
 * Demuestra el uso de enumeradores.
 */
public enum OrderStatus {
    PENDING("pending"),
    PAID("paid");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
