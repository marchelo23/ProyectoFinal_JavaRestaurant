package com.restaurant.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Order que representa un pedido del restaurante.
 * Demuestra composición con MenuItem.
 */
public class Order {
    private String orderId;
    private int tableNumber;
    private String waiterId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double taxRate;
    private String createdAt;
    private String paidAt;

    // Clase interna para representar un item en el pedido
    public static class OrderItem {
        private MenuItem item;
        private int quantity;

        public OrderItem() {
        }

        public OrderItem(MenuItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public MenuItem getItem() {
            return item;
        }

        public void setItem(MenuItem item) {
            this.item = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String orderId, int tableNumber, String waiterId) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.waiterId = waiterId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.taxRate = 0.16; // 16% IVA
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void addItem(MenuItem menuItem, int quantity) {
        // Buscar si el item ya existe
        for (OrderItem orderItem : items) {
            if (orderItem.getItem().getId().equals(menuItem.getId())) {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
                return;
            }
        }
        // Agregar nuevo item
        items.add(new OrderItem(menuItem, quantity));
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (OrderItem orderItem : items) {
            subtotal += orderItem.getItem().calculateTotal(orderItem.getQuantity());
        }
        return subtotal;
    }

    public double calculateTax() {
        return calculateSubtotal() * taxRate;
    }

    public double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }

    public void markAsPaid() {
        this.status = OrderStatus.PAID;
        this.paidAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        return "Pedido " + orderId + " - Mesa " + tableNumber +
                " - Estado: " + status + " - Total: $" + String.format("%.2f", calculateTotal());
    }
}
