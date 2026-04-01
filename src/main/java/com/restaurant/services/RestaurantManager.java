package com.restaurant.services;

import com.restaurant.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio principal que maneja la lógica de negocio del restaurante.
 */
public class RestaurantManager {
    private final FileStorage storage;
    private List<Table> tables;
    private List<Waiter> waiters;
    private List<MenuItem> menuItems;
    private List<Order> orders;

    public RestaurantManager() {
        this("data");
    }

    public RestaurantManager(String dataDir) {
        this.storage = new FileStorage(dataDir);
        this.tables = new ArrayList<>();
        this.waiters = new ArrayList<>();
        this.menuItems = new ArrayList<>();
        this.orders = new ArrayList<>();

        // Cargar datos existentes
        loadAllData();
    }

    public void loadAllData() {
        List<Table> loadedTables = storage.loadTables();
        if (loadedTables != null)
            this.tables = loadedTables;

        List<Waiter> loadedWaiters = storage.loadWaiters();
        if (loadedWaiters != null)
            this.waiters = loadedWaiters;

        List<MenuItem> loadedMenuItems = storage.loadMenuItems();
        if (loadedMenuItems != null)
            this.menuItems = loadedMenuItems;

        List<Order> loadedOrders = storage.loadOrders();
        if (loadedOrders != null)
            this.orders = loadedOrders;
    }

    public void saveAllData() {
        storage.saveTables(tables);
        storage.saveWaiters(waiters);
        storage.saveMenuItems(menuItems);
        storage.saveOrders(orders);
    }

    // ===== GESTIÓN DE MESAS =====

    public Table addTable(int tableNumber, int capacity) {
        if (getTable(tableNumber) != null) {
            throw new IllegalArgumentException("La mesa " + tableNumber + " ya existe");
        }

        Table table = new Table(tableNumber, capacity);
        tables.add(table);
        storage.saveTables(tables);
        return table;
    }

    public Table getTable(int tableNumber) {
        return tables.stream()
                .filter(t -> t.getTableNumber() == tableNumber)
                .findFirst()
                .orElse(null);
    }

    public List<Table> listTables() {
        return new ArrayList<>(tables);
    }

    // ===== GESTIÓN DE MESEROS =====

    public Waiter addWaiter(String id, String name, String phone, String employeeId) {
        if (getWaiter(id) != null) {
            throw new IllegalArgumentException("El mesero con ID " + id + " ya existe");
        }

        Waiter waiter = new Waiter(id, name, phone, employeeId);
        waiters.add(waiter);
        storage.saveWaiters(waiters);
        return waiter;
    }

    public Waiter getWaiter(String id) {
        return waiters.stream()
                .filter(w -> w.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Waiter> listWaiters() {
        return new ArrayList<>(waiters);
    }

    // ===== GESTIÓN DE MENÚ =====

    public FoodItem addFoodItem(String id, String name, double price, String category, int preparationTime) {
        if (getMenuItem(id) != null) {
            throw new IllegalArgumentException("El item con ID " + id + " ya existe");
        }

        FoodItem foodItem = new FoodItem(id, name, price, category, preparationTime);
        menuItems.add(foodItem);
        storage.saveMenuItems(menuItems);
        return foodItem;
    }

    public DrinkItem addDrinkItem(String id, String name, double price, String category, String size) {
        if (getMenuItem(id) != null) {
            throw new IllegalArgumentException("El item con ID " + id + " ya existe");
        }

        DrinkItem drinkItem = new DrinkItem(id, name, price, category, size);
        menuItems.add(drinkItem);
        storage.saveMenuItems(menuItems);
        return drinkItem;
    }

    public MenuItem getMenuItem(String id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<MenuItem> listMenuItems() {
        return new ArrayList<>(menuItems);
    }

    public List<FoodItem> listFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item instanceof FoodItem) {
                foodItems.add((FoodItem) item);
            }
        }
        return foodItems;
    }

    public List<DrinkItem> listDrinkItems() {
        List<DrinkItem> drinkItems = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item instanceof DrinkItem) {
                drinkItems.add((DrinkItem) item);
            }
        }
        return drinkItems;
    }

    // ===== GESTIÓN DE PEDIDOS =====

    public Order createOrder(String orderId, int tableNumber, String waiterId) {
        Table table = getTable(tableNumber);
        if (table == null) {
            throw new IllegalArgumentException("La mesa " + tableNumber + " no existe");
        }

        Waiter waiter = getWaiter(waiterId);
        if (waiter == null) {
            throw new IllegalArgumentException("El mesero con ID " + waiterId + " no existe");
        }

        if (getOrder(orderId) != null) {
            throw new IllegalArgumentException("El pedido " + orderId + " ya existe");
        }

        Order order = new Order(orderId, tableNumber, waiterId);
        orders.add(order);

        // Marcar mesa como ocupada
        table.occupy();

        saveAllData();
        return order;
    }

    public Order getOrder(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public List<Order> listOrders() {
        return new ArrayList<>(orders);
    }

    public void addItemToOrder(String orderId, String itemId, int quantity) {
        Order order = getOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Pedido " + orderId + " no encontrado");
        }

        MenuItem menuItem = getMenuItem(itemId);
        if (menuItem == null) {
            throw new IllegalArgumentException("Item " + itemId + " no encontrado");
        }

        order.addItem(menuItem, quantity);
        storage.saveOrders(orders);
    }

    public void payOrder(String orderId) {
        Order order = getOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Pedido " + orderId + " no encontrado");
        }

        order.markAsPaid();

        // Liberar la mesa
        Table table = getTable(order.getTableNumber());
        if (table != null) {
            table.free();
        }

        saveAllData();
    }

    public List<Order> getPendingOrders() {
        List<Order> pending = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PENDING) {
                pending.add(order);
            }
        }
        return pending;
    }

    public List<Order> getPendingOrdersByTable(int tableNumber) {
        List<Order> pendingByTable = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PENDING && order.getTableNumber() == tableNumber) {
                pendingByTable.add(order);
            }
        }
        return pendingByTable;
    }

    public List<Order> getPaidOrders() {
        List<Order> paid = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PAID) {
                paid.add(order);
            }
        }
        return paid;
    }
}
