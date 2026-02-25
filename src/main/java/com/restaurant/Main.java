package com.restaurant;

import com.restaurant.models.*;
import com.restaurant.services.ReportService;
import com.restaurant.services.RestaurantManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Clase principal con interfaz de consola para el sistema de gestión de
 * restaurante.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static RestaurantManager manager;
    private static ReportService reportService;

    public static void main(String[] args) {
        clearScreen();
        printHeader("SISTEMA DE GESTIÓN DE RESTAURANTE");
        System.out.println("Inicializando sistema...\n");

        manager = new RestaurantManager();
        reportService = new ReportService(manager);

        System.out.println("✓ Sistema iniciado correctamente");
        System.out.println("  • " + manager.listTables().size() + " mesas cargadas");
        System.out.println("  • " + manager.listWaiters().size() + " meseros cargados");
        System.out.println("  • " + manager.listMenuItems().size() + " items en menú");
        System.out.println("  • " + manager.listOrders().size() + " pedidos en historial");

        pause();
        mainMenu();

        scanner.close();
    }

    private static void mainMenu() {
        while (true) {
            clearScreen();
            printMenu("MENÚ PRINCIPAL", new String[] {
                    "Menú Administrador",
                    "Menú Mesero",
                    "Menú Cajero",
                    "Reportes",
                    "Salir"
            });

            int choice = readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    waiterMenu();
                    break;
                case 3:
                    cashierMenu();
                    break;
                case 4:
                    reportsMenu();
                    break;
                case 5:
                case 0:
                    System.out.println("\n¡Guardando datos y cerrando el sistema!");
                    manager.saveAllData();
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pause();
            }
        }
    }

    // ===== MENÚ ADMINISTRADOR =====

    private static void adminMenu() {
        while (true) {
            clearScreen();
            printMenu("MENÚ ADMINISTRADOR", new String[] {
                    "Registrar Mesa",
                    "Listar Mesas",
                    "Registrar Mesero",
                    "Listar Meseros",
                    "Registrar Platillo (Comida)",
                    "Registrar Bebida",
                    "Listar Menú"
            });

            int choice = readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    registerTable();
                    break;
                case 2:
                    listTables();
                    break;
                case 3:
                    registerWaiter();
                    break;
                case 4:
                    listWaiters();
                    break;
                case 5:
                    registerFoodItem();
                    break;
                case 6:
                    registerDrinkItem();
                    break;
                case 7:
                    listMenuItems();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pause();
            }
        }
    }

    private static void registerTable() {
        printHeader("REGISTRAR MESA");
        try {
            int tableNumber = readInt("Número de mesa: ");
            int capacity = readInt("Capacidad (personas): ");

            Table table = manager.addTable(tableNumber, capacity);
            System.out.println("\n✓ Mesa registrada exitosamente: " + table);
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void listTables() {
        printHeader("LISTADO DE MESAS");
        List<Table> tables = manager.listTables();

        if (tables.isEmpty()) {
            System.out.println("No hay mesas registradas.");
        } else {
            for (Table table : tables) {
                System.out.println("  • " + table);
            }
        }
        pause();
    }

    private static void registerWaiter() {
        printHeader("REGISTRAR MESERO");
        try {
            System.out.print("ID del mesero: ");
            String id = scanner.nextLine();
            System.out.print("Nombre completo: ");
            String name = scanner.nextLine();
            System.out.print("Teléfono: ");
            String phone = scanner.nextLine();
            System.out.print("ID de empleado: ");
            String employeeId = scanner.nextLine();

            Waiter waiter = manager.addWaiter(id, name, phone, employeeId);
            System.out.println("\n✓ Mesero registrado exitosamente: " + waiter);
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void listWaiters() {
        printHeader("LISTADO DE MESEROS");
        List<Waiter> waiters = manager.listWaiters();

        if (waiters.isEmpty()) {
            System.out.println("No hay meseros registrados.");
        } else {
            for (Waiter waiter : waiters) {
                System.out.println("  • " + waiter);
            }
        }
        pause();
    }

    private static void registerFoodItem() {
        printHeader("REGISTRAR PLATILLO (COMIDA)");
        try {
            System.out.print("ID del platillo: ");
            String id = scanner.nextLine();
            System.out.print("Nombre del platillo: ");
            String name = scanner.nextLine();
            double price = readDouble("Precio: $");
            System.out.print("Categoría (ej: entrada, plato fuerte, postre): ");
            String category = scanner.nextLine();
            int prepTime = readInt("Tiempo de preparación (minutos): ");

            FoodItem foodItem = manager.addFoodItem(id, name, price, category, prepTime);
            System.out.println("\n✓ Platillo registrado exitosamente: " + foodItem);
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void registerDrinkItem() {
        printHeader("REGISTRAR BEBIDA");
        try {
            System.out.print("ID de la bebida: ");
            String id = scanner.nextLine();
            System.out.print("Nombre de la bebida: ");
            String name = scanner.nextLine();
            double price = readDouble("Precio: $");
            System.out.print("Categoría (ej: refresco, jugo, alcohol): ");
            String category = scanner.nextLine();
            System.out.print("Tamaño (ej: pequeño, mediano, grande): ");
            String size = scanner.nextLine();

            DrinkItem drinkItem = manager.addDrinkItem(id, name, price, category, size);
            System.out.println("\n✓ Bebida registrada exitosamente: " + drinkItem);
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void listMenuItems() {
        printHeader("MENÚ DEL RESTAURANTE");
        List<MenuItem> menuItems = manager.listMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("No hay items en el menú.");
        } else {
            System.out.println("COMIDAS:");
            List<FoodItem> foodItems = manager.listFoodItems();
            if (foodItems.isEmpty()) {
                System.out.println("  (ninguna)");
            } else {
                for (FoodItem item : foodItems) {
                    System.out.println("  • " + item);
                }
            }

            System.out.println("\nBEBIDAS:");
            List<DrinkItem> drinkItems = manager.listDrinkItems();
            if (drinkItems.isEmpty()) {
                System.out.println("  (ninguna)");
            } else {
                for (DrinkItem item : drinkItems) {
                    System.out.println("  • " + item);
                }
            }
        }
        pause();
    }

    // ===== MENÚ MESERO =====

    private static void waiterMenu() {
        while (true) {
            clearScreen();
            printMenu("MENÚ MESERO", new String[] {
                    "Crear Pedido",
                    "Agregar Items a Pedido",
                    "Ver Pedidos Pendientes",
                    "Ver Detalle de Pedido"
            });

            int choice = readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    addItemsToOrder();
                    break;
                case 3:
                    viewPendingOrders();
                    break;
                case 4:
                    viewOrderDetail();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pause();
            }
        }
    }

    private static void createOrder() {
        printHeader("CREAR PEDIDO");
        try {
            System.out.print("ID del pedido: ");
            String orderId = scanner.nextLine();
            int tableNumber = readInt("Número de mesa: ");
            System.out.print("ID del mesero: ");
            String waiterId = scanner.nextLine();

            Order order = manager.createOrder(orderId, tableNumber, waiterId);
            System.out.println("\n✓ Pedido creado exitosamente: " + order);
            System.out.println("  Mesa marcada como ocupada.");
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void addItemsToOrder() {
        printHeader("AGREGAR ITEMS A PEDIDO");
        try {
            System.out.print("ID del pedido: ");
            String orderId = scanner.nextLine();

            Order order = manager.getOrder(orderId);
            if (order == null) {
                System.out.println("\n✗ Pedido " + orderId + " no encontrado.");
                pause();
                return;
            }

            System.out.println("\nPedido actual: " + order);
            System.out.println("Total actual: $" + String.format("%.2f", order.calculateTotal()));

            // Mostrar menú disponible
            System.out.println("\n--- MENÚ DISPONIBLE ---");
            List<MenuItem> menuItems = manager.listMenuItems();
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + menuItems.get(i));
            }

            while (true) {
                System.out.println("\n--- AGREGAR ITEM ---");
                System.out.print("ID del item (o 'fin' para terminar): ");
                String itemId = scanner.nextLine();

                if (itemId.equalsIgnoreCase("fin")) {
                    break;
                }

                int quantity = readInt("Cantidad: ");
                manager.addItemToOrder(orderId, itemId, quantity);

                System.out.println("\n✓ Item agregado al pedido.");
                System.out.println("  Nuevo total: $" + String.format("%.2f", order.calculateTotal()));
            }

            System.out.println("\n✓ Pedido actualizado: " + order);
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void viewPendingOrders() {
        printHeader("PEDIDOS PENDIENTES");
        List<Order> pending = manager.getPendingOrders();

        if (pending.isEmpty()) {
            System.out.println("No hay pedidos pendientes.");
        } else {
            for (Order order : pending) {
                System.out.println("  • " + order);
            }
        }
        pause();
    }

    private static void viewOrderDetail() {
        printHeader("DETALLE DE PEDIDO");
        System.out.print("ID del pedido: ");
        String orderId = scanner.nextLine();

        Order order = manager.getOrder(orderId);
        if (order == null) {
            System.out.println("\n✗ Pedido " + orderId + " no encontrado.");
        } else {
            System.out.println("\nPedido ID: " + order.getOrderId());
            System.out.println("Mesa: " + order.getTableNumber());
            System.out.println("Mesero ID: " + order.getWaiterId());
            System.out.println("Estado: " + order.getStatus());
            System.out.println("Creado: " + order.getCreatedAt());

            if (!order.getItems().isEmpty()) {
                System.out.println("\nItems:");
                for (Order.OrderItem item : order.getItems()) {
                    MenuItem menuItem = item.getItem();
                    int quantity = item.getQuantity();
                    double subtotal = menuItem.calculateTotal(quantity);
                    System.out.println("  • " + menuItem.getName() + " x" + quantity +
                            " = $" + String.format("%.2f", subtotal));
                }
            } else {
                System.out.println("\nItems: (ninguno)");
            }

            System.out.println("\nSubtotal: $" + String.format("%.2f", order.calculateSubtotal()));
            System.out.println("Impuesto (" + (int) (order.getTaxRate() * 100) + "%): $" +
                    String.format("%.2f", order.calculateTax()));
            System.out.println("TOTAL: $" + String.format("%.2f", order.calculateTotal()));
        }
        pause();
    }

    // ===== MENÚ CAJERO =====

    private static void cashierMenu() {
        while (true) {
            clearScreen();
            printMenu("MENÚ CAJERO", new String[] {
                    "Registrar Pago de Pedido",
                    "Ver Pedidos Pendientes de Pago",
                    "Ver Pedidos Pagados Hoy"
            });

            int choice = readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    processPayment();
                    break;
                case 2:
                    viewPendingOrders();
                    break;
                case 3:
                    viewPaidOrdersToday();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pause();
            }
        }
    }

    private static void processPayment() {
        printHeader("REGISTRAR PAGO");
        try {
            System.out.print("ID del pedido: ");
            String orderId = scanner.nextLine();

            Order order = manager.getOrder(orderId);
            if (order == null) {
                System.out.println("\n✗ Pedido " + orderId + " no encontrado.");
                pause();
                return;
            }

            System.out.println("\nPedido: " + order);
            System.out.println("\nSubtotal: $" + String.format("%.2f", order.calculateSubtotal()));
            System.out.println("Impuesto (" + (int) (order.getTaxRate() * 100) + "%): $" +
                    String.format("%.2f", order.calculateTax()));
            System.out.println("TOTAL A PAGAR: $" + String.format("%.2f", order.calculateTotal()));

            System.out.print("\n¿Confirmar pago? (s/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("s")) {
                manager.payOrder(orderId);
                System.out.println("\n✓ Pago registrado exitosamente.");
                System.out.println("  Mesa " + order.getTableNumber() + " liberada.");
            } else {
                System.out.println("\n✗ Pago cancelado.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
        pause();
    }

    private static void viewPaidOrdersToday() {
        printHeader("PEDIDOS PAGADOS HOY");
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<Order> paid = manager.getPaidOrders();

        List<Order> paidToday = new java.util.ArrayList<>();
        for (Order order : paid) {
            if (order.getPaidAt() != null && order.getPaidAt().startsWith(today)) {
                paidToday.add(order);
            }
        }

        if (paidToday.isEmpty()) {
            System.out.println("No hay pedidos pagados hoy.");
        } else {
            for (Order order : paidToday) {
                System.out.println("  • " + order);
            }
        }
        pause();
    }

    // ===== MENÚ REPORTES =====

    private static void reportsMenu() {
        while (true) {
            clearScreen();
            printMenu("MENÚ REPORTES", new String[] {
                    "Reporte de Ventas Diario",
                    "Ventas por Mesero",
                    "Items Más Populares"
            });

            int choice = readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    dailySalesReport();
                    break;
                case 2:
                    salesByWaiterReport();
                    break;
                case 3:
                    popularItemsReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pause();
            }
        }
    }

    private static void dailySalesReport() {
        printHeader("REPORTE DE VENTAS DIARIO");
        System.out.print("Fecha (YYYY-MM-DD) o Enter para hoy: ");
        String dateInput = scanner.nextLine();
        String date = dateInput.isEmpty() ? null : dateInput;

        ReportService.DailySalesReport report = reportService.generateDailySalesReport(date);

        System.out.println("\nFecha: " + report.getDate());
        System.out.println("Total de pedidos: " + report.getTotalOrders());
        System.out.println("Subtotal: $" + String.format("%.2f", report.getSubtotal()));
        System.out.println("Impuestos: $" + String.format("%.2f", report.getTax()));
        System.out.println("VENTAS TOTALES: $" + String.format("%.2f", report.getTotalSales()));

        if (!report.getOrders().isEmpty()) {
            System.out.println("\nDetalle de pedidos:");
            for (Order order : report.getOrders()) {
                System.out.println("  • Pedido " + order.getOrderId() + " - Mesa " +
                        order.getTableNumber() + " - $" + String.format("%.2f", order.calculateTotal()));
            }
        }
        pause();
    }

    private static void salesByWaiterReport() {
        printHeader("VENTAS POR MESERO");
        System.out.print("Fecha (YYYY-MM-DD) o Enter para hoy: ");
        String dateInput = scanner.nextLine();
        String date = dateInput.isEmpty() ? null : dateInput;

        Map<String, ReportService.WaiterSales> waiterSales = reportService.getSalesByWaiter(date);

        if (waiterSales.isEmpty()) {
            System.out.println("\nNo hay ventas registradas para esta fecha.");
        } else {
            System.out.println(
                    "\nFecha: " + (date != null ? date : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)));
            System.out.println("\nVentas por mesero:");
            for (ReportService.WaiterSales sales : waiterSales.values()) {
                System.out.println("  • Mesero " + sales.getWaiterId() + ": " +
                        sales.getOrders() + " pedidos - $" + String.format("%.2f", sales.getTotalSales()));
            }
        }
        pause();
    }

    private static void popularItemsReport() {
        printHeader("ITEMS MÁS POPULARES");
        System.out.print("Fecha (YYYY-MM-DD) o Enter para hoy: ");
        String dateInput = scanner.nextLine();
        String date = dateInput.isEmpty() ? null : dateInput;

        List<ReportService.PopularItem> popularItems = reportService.getPopularItems(date);

        if (popularItems.isEmpty()) {
            System.out.println("\nNo hay datos de ventas para esta fecha.");
        } else {
            System.out.println(
                    "\nFecha: " + (date != null ? date : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)));
            System.out.println("\nItems ordenados por popularidad:");
            for (int i = 0; i < popularItems.size(); i++) {
                ReportService.PopularItem item = popularItems.get(i);
                System.out.println("  " + (i + 1) + ". " + item.getName() + ": " +
                        item.getQuantity() + " unidades - $" + String.format("%.2f", item.getRevenue()));
            }
        }
        pause();
    }

    // ===== UTILIDADES =====

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60) + "\n");
    }

    private static void printMenu(String title, String[] options) {
        printHeader(title);
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
        System.out.println("  0. Volver\n");
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }

    private static void pause() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
