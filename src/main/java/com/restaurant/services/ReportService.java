package com.restaurant.services;

import com.restaurant.models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Servicio para generar reportes de ventas y estadísticas.
 */
public class ReportService {
    private final RestaurantManager manager;

    public ReportService(RestaurantManager manager) {
        this.manager = manager;
    }

    /**
     * Genera reporte de ventas diario.
     */
    public DailySalesReport generateDailySalesReport(String date) {
        if (date == null) {
            date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        List<Order> dailyOrders = filterOrdersByDate(date);

        double subtotal = 0.0;
        double tax = 0.0;
        double total = 0.0;

        for (Order order : dailyOrders) {
            subtotal += order.calculateSubtotal();
            tax += order.calculateTax();
            total += order.calculateTotal();
        }

        return new DailySalesReport(date, dailyOrders.size(), subtotal, tax, total, dailyOrders);
    }

    /**
     * Obtiene ventas agrupadas por mesero.
     */
    public Map<String, WaiterSales> getSalesByWaiter(String date) {
        if (date == null) {
            date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        List<Order> dailyOrders = filterOrdersByDate(date);
        Map<String, WaiterSales> salesMap = new HashMap<>();

        for (Order order : dailyOrders) {
            String waiterId = order.getWaiterId();
            WaiterSales sales = salesMap.getOrDefault(waiterId, new WaiterSales(waiterId));
            sales.addOrder(order.calculateTotal());
            salesMap.put(waiterId, sales);
        }

        return salesMap;
    }

    /**
     * Obtiene los items más populares.
     */
    public List<PopularItem> getPopularItems(String date) {
        if (date == null) {
            date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        List<Order> dailyOrders = filterOrdersByDate(date);
        Map<String, PopularItem> itemsMap = new HashMap<>();

        for (Order order : dailyOrders) {
            for (Order.OrderItem orderItem : order.getItems()) {
                MenuItem menuItem = orderItem.getItem();
                int quantity = orderItem.getQuantity();
                double revenue = menuItem.calculateTotal(quantity);

                PopularItem popularItem = itemsMap.getOrDefault(menuItem.getId(),
                        new PopularItem(menuItem.getId(), menuItem.getName()));
                popularItem.addSale(quantity, revenue);
                itemsMap.put(menuItem.getId(), popularItem);
            }
        }

        List<PopularItem> items = new ArrayList<>(itemsMap.values());
        items.sort((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()));
        return items;
    }

    private List<Order> filterOrdersByDate(String date) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : manager.listOrders()) {
            if (order.getStatus() == OrderStatus.PAID &&
                    order.getPaidAt() != null &&
                    order.getPaidAt().startsWith(date)) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    // Clases internas para reportes

    public static class DailySalesReport {
        private final String date;
        private final int totalOrders;
        private final double subtotal;
        private final double tax;
        private final double totalSales;
        private final List<Order> orders;

        public DailySalesReport(String date, int totalOrders, double subtotal,
                double tax, double totalSales, List<Order> orders) {
            this.date = date;
            this.totalOrders = totalOrders;
            this.subtotal = subtotal;
            this.tax = tax;
            this.totalSales = totalSales;
            this.orders = orders;
        }

        public String getDate() {
            return date;
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getTax() {
            return tax;
        }

        public double getTotalSales() {
            return totalSales;
        }

        public List<Order> getOrders() {
            return orders;
        }
    }

    public static class WaiterSales {
        private final String waiterId;
        private int orders;
        private double totalSales;

        public WaiterSales(String waiterId) {
            this.waiterId = waiterId;
            this.orders = 0;
            this.totalSales = 0.0;
        }

        public void addOrder(double amount) {
            this.orders++;
            this.totalSales += amount;
        }

        public String getWaiterId() {
            return waiterId;
        }

        public int getOrders() {
            return orders;
        }

        public double getTotalSales() {
            return totalSales;
        }
    }

    public static class PopularItem {
        private final String itemId;
        private final String name;
        private int quantity;
        private double revenue;

        public PopularItem(String itemId, String name) {
            this.itemId = itemId;
            this.name = name;
            this.quantity = 0;
            this.revenue = 0.0;
        }

        public void addSale(int qty, double rev) {
            this.quantity += qty;
            this.revenue += rev;
        }

        public String getItemId() {
            return itemId;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getRevenue() {
            return revenue;
        }
    }
}
