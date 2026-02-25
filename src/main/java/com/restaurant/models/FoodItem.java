package com.restaurant.models;

/**
 * Clase FoodItem que hereda de MenuItem.
 * Representa un platillo de comida.
 */
public class FoodItem extends MenuItem {
    private int preparationTime; // en minutos

    public FoodItem() {
        super();
    }

    public FoodItem(String id, String name, double price, String category, int preparationTime) {
        super(id, name, price, category);
        this.preparationTime = preparationTime;
    }

    @Override
    public double calculateTotal(int quantity) {
        return getPrice() * quantity;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", preparationTime=" + preparationTime + " min" +
                '}';
    }
}
