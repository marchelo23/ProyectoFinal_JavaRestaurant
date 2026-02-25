package com.restaurant.models;

/**
 * Clase abstracta MenuItem que representa un item del menú.
 * Demuestra el principio de abstracción.
 */
public abstract class MenuItem {
    private String id;
    private String name;
    private double price;
    private String category;

    public MenuItem() {
    }

    public MenuItem(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Calcula el total para una cantidad dada.
     */
    public abstract double calculateTotal(int quantity);

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
