package com.restaurant.models;

/**
 * Clase DrinkItem que hereda de MenuItem.
 * Representa una bebida.
 */
public class DrinkItem extends MenuItem {
    private String size; // pequeño, mediano, grande

    public DrinkItem() {
        super();
    }

    public DrinkItem(String id, String name, double price, String category, String size) {
        super(id, name, price, category);
        this.size = size;
    }

    @Override
    public double calculateTotal(int quantity) {
        return getPrice() * quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DrinkItem{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", size='" + size + '\'' +
                '}';
    }
}
